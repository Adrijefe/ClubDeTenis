package com.example.clubdetenis.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clubdetenis.R;
import com.example.clubdetenis.Utils.PreferenceManager;
import com.example.clubdetenis.api.ApiClient;
import com.example.clubdetenis.api.ApiService;
import com.example.clubdetenis.models.Pista;
import com.example.clubdetenis.models.ReservaRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearReservaActivity extends AppCompatActivity {
    private Spinner spinnerPistas;
    private DatePicker datePicker;
    private Spinner spinnerHoras;
    private Button btnReservar;
    private List<Pista> pistas = new ArrayList<>();
    private List<String> horasDisponibles = new ArrayList<>();
    private PreferenceManager preferenceManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_reserva);

        preferenceManager = new PreferenceManager(this);
        int usuarioId = preferenceManager.getUser().getId();

        spinnerPistas = findViewById(R.id.spinnerPistas);
        datePicker = findViewById(R.id.datePicker);
        spinnerHoras = findViewById(R.id.spinnerHoras);
        btnReservar = findViewById(R.id.btnReservar);

        // Cargar las pistas disponibles
        cargarPistas();

        // Establecer un listener para que actualice las horas disponibles cada vez que cambie la fecha
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                (view, year, monthOfYear, dayOfMonth) -> cargarHorasDisponibles());

        // Establecer listener para cuando cambie la pista seleccionada
        spinnerPistas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cargarHorasDisponibles();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        // Accionar la reserva cuando el botón es presionado
        btnReservar.setOnClickListener(v -> crearReserva(usuarioId));
    }

    private void cargarPistas() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getPistasDisponibles().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JsonObject json = response.body();
                        JsonArray pistasJson = json.getAsJsonArray("pistas");

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Pista>>(){}.getType();
                        pistas = gson.fromJson(pistasJson, listType);

                        ArrayAdapter<Pista> adapter = new ArrayAdapter<>(
                                CrearReservaActivity.this,
                                android.R.layout.simple_spinner_item,
                                pistas
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerPistas.setAdapter(adapter);
                    } catch (Exception e) {
                        Log.e("CrearReserva", "Error parsing pistas", e);
                        Toast.makeText(CrearReservaActivity.this, "Error al cargar pistas", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CrearReservaActivity.this, "No se pudo cargar las pistas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(CrearReservaActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarHorasDisponibles() {
        String fecha = String.format("%04d-%02d-%02d",
                datePicker.getYear(),
                datePicker.getMonth() + 1,
                datePicker.getDayOfMonth());

        Pista pistaSeleccionada = (Pista) spinnerPistas.getSelectedItem();
        if (fecha.isEmpty() || pistaSeleccionada == null) {
            return;
        }

        int pistaId = pistaSeleccionada.getId();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getHorasDisponibles(fecha, pistaId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JsonObject json = response.body();
                        JsonArray horasJson = json.getAsJsonArray("horas");

                        horasDisponibles.clear();
                        for (int i = 0; i < horasJson.size(); i++) {
                            horasDisponibles.add(horasJson.get(i).getAsString());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                CrearReservaActivity.this,
                                android.R.layout.simple_spinner_item,
                                horasDisponibles
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerHoras.setAdapter(adapter);

                    } catch (Exception e) {
                        Log.e("CrearReserva", "Error parsing horas", e);
                        Toast.makeText(CrearReservaActivity.this, "Error al cargar las horas disponibles", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CrearReservaActivity.this, "No se pudieron cargar las horas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(CrearReservaActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void crearReserva(int usuarioId) {
        Pista pistaSeleccionada = (Pista) spinnerPistas.getSelectedItem();
        String horaSeleccionada = (String) spinnerHoras.getSelectedItem();

        if (pistaSeleccionada == null || horaSeleccionada == null) {
            Toast.makeText(CrearReservaActivity.this, "Debe seleccionar una pista y una hora", Toast.LENGTH_SHORT).show();
            return;
        }

        String fecha = String.format("%04d-%02d-%02d",
                datePicker.getYear(),
                datePicker.getMonth() + 1,
                datePicker.getDayOfMonth());

        ReservaRequest reservaRequest = new ReservaRequest(
                usuarioId,
                pistaSeleccionada.getId(),
                fecha,
                horaSeleccionada,
                incrementarHora(horaSeleccionada)
        );

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.crearReserva(reservaRequest).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        Toast.makeText(CrearReservaActivity.this, "Reserva creada con éxito", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CrearReservaActivity.this, "Error al crear la reserva", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(CrearReservaActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String incrementarHora(String hora) {
        String[] parts = hora.split(":");
        int horaInicio = Integer.parseInt(parts[0]);
        int minutoInicio = Integer.parseInt(parts[1]);
        int horaFin = (horaInicio + 1) % 24;
        return String.format("%02d:%02d:00", horaFin, minutoInicio);
    }
}
