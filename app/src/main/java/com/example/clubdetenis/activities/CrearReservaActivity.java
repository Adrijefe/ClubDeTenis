package com.example.clubdetenis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.clubdetenis.PistaResponse;
import com.example.clubdetenis.R;
import com.example.clubdetenis.Utils.PreferenceManager;
import com.example.clubdetenis.api.ApiClient;
import com.example.clubdetenis.api.ApiService;
import com.example.clubdetenis.models.Pista;
import com.example.clubdetenis.models.ReservaRequest;
import com.example.clubdetenis.models.Usuario;

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
    private int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_reserva);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferenceManager = new PreferenceManager(this);
        Usuario usuario = preferenceManager.getUser();
        if (usuario != null) {
            usuarioId = usuario.getId();
        } else {
            Toast.makeText(this, "Error: usuario no logueado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        spinnerPistas = findViewById(R.id.spinnerPistas);
        datePicker = findViewById(R.id.datePicker);
        spinnerHoras = findViewById(R.id.spinnerHoras);
        btnReservar = findViewById(R.id.btnReservar);

        datePicker.setMinDate(System.currentTimeMillis());
        cargarPistas();

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                (view, year, monthOfYear, dayOfMonth) -> cargarHorasDisponibles());

        spinnerPistas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cargarHorasDisponibles();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        btnReservar.setOnClickListener(v -> crearReserva(usuarioId));
    }
    private void cargarPistas() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getPistasDisponibles().enqueue(new Callback<PistaResponse>() {
            @Override
            public void onResponse(Call<PistaResponse> call, Response<PistaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PistaResponse pistasResponse = response.body();

                    if (pistasResponse.isSuccess()) {
                        pistas = pistasResponse.getPistas();
                        ArrayAdapter<Pista> adapter = new ArrayAdapter<>(CrearReservaActivity.this,
                                android.R.layout.simple_spinner_item, pistas);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerPistas.setAdapter(adapter);
                    } else {
                        Toast.makeText(CrearReservaActivity.this, "No hay pistas disponibles", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CrearReservaActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PistaResponse> call, Throwable t) {
                Toast.makeText(CrearReservaActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void cargarHorasDisponibles() {
        String fechaApi = getFechaFormatoApi();
        Pista pistaSeleccionada = (Pista) spinnerPistas.getSelectedItem();
        if (fechaApi.isEmpty() || pistaSeleccionada == null) {
            return;
        }

        int pistaId = pistaSeleccionada.getId();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getHorasDisponibles(fechaApi, pistaId).enqueue(new Callback<com.google.gson.JsonObject>() {
            @Override
            public void onResponse(Call<com.google.gson.JsonObject> call, Response<com.google.gson.JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        com.google.gson.JsonObject json = response.body();
                        com.google.gson.JsonArray horasJson = json.getAsJsonArray("horas");

                        horasDisponibles.clear();
                        for (int i = 0; i < horasJson.size(); i++) {
                            horasDisponibles.add(horasJson.get(i).getAsString());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(CrearReservaActivity.this,
                                android.R.layout.simple_spinner_item, horasDisponibles);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerHoras.setAdapter(adapter);
                    } catch (Exception e) {
                        Toast.makeText(CrearReservaActivity.this, "Error al cargar las horas disponibles", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<com.google.gson.JsonObject> call, Throwable t) {
                Toast.makeText(CrearReservaActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void crearReserva(int usuarioId) {
        Pista pistaSeleccionada = (Pista) spinnerPistas.getSelectedItem();
        String horaSeleccionada = (String) spinnerHoras.getSelectedItem();

        if (pistaSeleccionada == null || horaSeleccionada == null) {
            Toast.makeText(CrearReservaActivity.this, "Debes seleccionar una pista y una hora", Toast.LENGTH_SHORT).show();
            return;
        }

        String fechaApi = getFechaFormatoApi();

        ReservaRequest reservaRequest = new ReservaRequest(usuarioId, pistaSeleccionada.getId(), fechaApi, horaSeleccionada, incrementarHora(horaSeleccionada));

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.crearReserva(reservaRequest).enqueue(new Callback<com.google.gson.JsonObject>() {

            @Override
            public void onResponse(Call<com.google.gson.JsonObject> call, Response<com.google.gson.JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.google.gson.JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        Toast.makeText(CrearReservaActivity.this, "Reserva creada con éxito", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // Verificar si hay mensaje de error en el JSON
                        if (json.has("message") && !json.get("message").isJsonNull()) {
                            Toast.makeText(CrearReservaActivity.this, json.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(CrearReservaActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<com.google.gson.JsonObject> call, Throwable t) {
                Toast.makeText(CrearReservaActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String incrementarHora(String hora) {
        String[] parts = hora.split(":");
        int horaInicio = Integer.parseInt(parts[0]);
        int minutoInicio = Integer.parseInt(parts[1]);
        int horaFin = (horaInicio + 1) % 24;
        return String.format("%02d:%02d", horaFin, minutoInicio);
    }

    private String getFechaFormatoApi() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        MenuItem menuUsuarios = menu.findItem(R.id.menu_usuarios);

        Usuario loggedUser = preferenceManager.getUser(); // Obtener el usuario logueado

        if (menuUsuarios != null) {
            if (loggedUser != null && "Administrador".equals(loggedUser.getPerfil())) {
                menuUsuarios.setVisible(true);
            } else {
                menuUsuarios.setVisible(false);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Usuario loggedUser = preferenceManager.getUser();

        if (id == R.id.menu_crear_reserva) {
            Toast.makeText(this, "Ya estás en Crear Reserva", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_pistas) {
            startActivity(new Intent(this, PistasActivity.class));
            return true;
        } else if (id == R.id.menu_reservas) {
            startActivity(new Intent(this, ReservasActivity.class));
            return true;
        } else if (id == R.id.menu_usuarios) {
            if (loggedUser != null && "Administrador".equals(loggedUser.getPerfil())) {
                startActivity(new Intent(this, MenuUsuariosActivity.class));
            } else {
                Toast.makeText(this, "No tienes permisos para acceder", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.menu_logout) {
            preferenceManager.clear();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
