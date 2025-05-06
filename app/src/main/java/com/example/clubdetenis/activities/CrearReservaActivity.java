package com.example.clubdetenis.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
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
    private TimePicker timePicker;
    private Button btnReservar;
    private List<Pista> pistas = new ArrayList<>();
    private PreferenceManager preferenceManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_reserva);

        preferenceManager = new PreferenceManager(this);
        int usuarioId = preferenceManager.getUser().getId();

        // Inicializar vistas
        spinnerPistas = findViewById(R.id.spinnerPistas);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        btnReservar = findViewById(R.id.btnReservar);
        timePicker.setIs24HourView(true);

        // Cargar pistas disponibles
        cargarPistas();

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
                        // Cambiar "pista" a "pistas" para que coincida con el JSON
                        JsonArray pistasJson = json.getAsJsonArray("pistas");

                        // Convertir el JSON en una lista de objetos Pista
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Pista>>(){}.getType();
                        pistas = gson.fromJson(pistasJson, listType);

                        // Crear un ArrayAdapter y asociarlo al Spinner
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

    private void crearReserva(int usuarioId) {
        Pista pistaSeleccionada = (Pista) spinnerPistas.getSelectedItem();

        if (pistaSeleccionada == null) {
            Toast.makeText(CrearReservaActivity.this, "Debes seleccionar una pista", Toast.LENGTH_SHORT).show();
            return;
        }

        String fecha = String.format("%04d-%02d-%02d",
                datePicker.getYear(),
                datePicker.getMonth() + 1,
                datePicker.getDayOfMonth());

        // Obtener la hora de inicio del TimePicker
        int horaInicio = timePicker.getHour();
        int minutoInicio = timePicker.getMinute();

        // Calcular la hora final (sumamos una hora al valor de horaInicio)
        int horaFin = (horaInicio + 1) % 24;  // % 24 para no superar las 24 horas
        int minutoFin = minutoInicio;         // Los minutos permanecen iguales

        // Formatear las horas en formato "HH:mm:ss"
        String horaInicioStr = String.format("%02d:%02d:00", horaInicio, minutoInicio);
        String horaFinStr = String.format("%02d:%02d:00", horaFin, minutoFin);

        // Log para verificar los datos que se están enviando
        Log.d("CrearReserva", "Datos de reserva: ");
        Log.d("CrearReserva", "Usuario ID: " + usuarioId);
        Log.d("CrearReserva", "Pista ID: " + pistaSeleccionada.getId());
        Log.d("CrearReserva", "Fecha: " + fecha);
        Log.d("CrearReserva", "Hora Inicio: " + horaInicioStr);
        Log.d("CrearReserva", "Hora Fin: " + horaFinStr);

        // Validación para asegurarse de que todos los datos son válidos antes de enviarlos
        if (fecha.isEmpty() || horaInicioStr.isEmpty() || horaFinStr.isEmpty()) {
            Toast.makeText(CrearReservaActivity.this, "Faltan datos en la reserva", Toast.LENGTH_SHORT).show();
            return;
        }

        ReservaRequest reservaRequest = new ReservaRequest(
                usuarioId,
                pistaSeleccionada.getId(),
                fecha,
                horaInicioStr,
                horaFinStr
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
                        // Manejar el caso cuando la API devuelve un mensaje de error
                        if (json.has("message")) {
                            Toast.makeText(CrearReservaActivity.this, json.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CrearReservaActivity.this, "Error desconocido al crear la reserva", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(CrearReservaActivity.this, "Error al crear la reserva", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(CrearReservaActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
