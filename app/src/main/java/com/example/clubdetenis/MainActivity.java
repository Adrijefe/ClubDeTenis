package com.example.clubdetenis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdetenis.Adapter.ReservasAdapter;
import com.example.clubdetenis.Utils.PreferenceManager;
import com.example.clubdetenis.activities.CrearReservaActivity;
import com.example.clubdetenis.activities.LoginActivity;
import com.example.clubdetenis.activities.PistasActivity;
import com.example.clubdetenis.activities.ReservasActivity;
import com.example.clubdetenis.activities.UsuariosActivity;  // <-- Importar UsuariosActivity
import com.example.clubdetenis.api.ApiClient;
import com.example.clubdetenis.api.ApiService;
import com.example.clubdetenis.models.Reserva;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button btnPistas, btnReservas, btnLogout, btnReservar, btnUsuarios;  // <-- Botón para usuarios
    private PreferenceManager preferenceManager;

    private RecyclerView recyclerView;
    private ReservasAdapter adapter;
    private List<Reserva> reservaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(this);
        if (!preferenceManager.isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        btnPistas = findViewById(R.id.btnPistas);
        btnReservas = findViewById(R.id.btnReservas);
        btnLogout = findViewById(R.id.btnLogout);
        btnReservar = findViewById(R.id.btnNuevaReserva);
        btnUsuarios = findViewById(R.id.btnUsuarios);  // <-- Botón para usuarios

        // Navegar a PistasActivity
        btnPistas.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PistasActivity.class)));

        // Navegar a ReservasActivity
        btnReservas.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ReservasActivity.class)));

        // Navegar a CrearReservaActivity
        btnReservar.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CrearReservaActivity.class)));

        // Cerrar sesión y navegar a LoginActivity
        btnLogout.setOnClickListener(v -> {
            preferenceManager.clear();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        // Navegar a UsuariosActivity
        btnUsuarios.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UsuariosActivity.class)));  // <-- Navegación correcta

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.reservasListado);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReservasAdapter(this, reservaList);
        recyclerView.setAdapter(adapter);

        // Cargar reservas próximas al iniciar
        loadProximasReservas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar reservas cuando vuelve a primer plano (por si se eliminaron en ReservasActivity)
        loadProximasReservas();
    }

    private void loadProximasReservas() {
        int usuarioId = preferenceManager.getUser().getId();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<JsonObject> call = apiService.getReservas(usuarioId);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JsonObject json = response.body();
                        JsonArray reservasJson = json.getAsJsonArray("reservas");

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Reserva>>() {}.getType();
                        List<Reserva> todasReservas = gson.fromJson(reservasJson, listType);

                        // Filtrar reservas para hoy
                        List<Reserva> reservasHoy = filtrarReservasHoy(todasReservas);

                        if (reservasHoy.isEmpty()) {
                            Toast.makeText(MainActivity.this, "No tienes reservas para hoy.", Toast.LENGTH_SHORT).show();
                        }

                        // Actualizar datos en el adaptador siempre, incluso si está vacío
                        adapter.updateData(reservasHoy);

                    } catch (Exception e) {
                        Log.e("MainActivity", "Error al parsear JSON", e);
                        Toast.makeText(MainActivity.this, "Error al procesar datos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error al cargar reservas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("MainActivity", "Error en la conexión", t);
                Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Reserva> filtrarReservasHoy(List<Reserva> todasReservas) {
        List<Reserva> reservasHoy = new ArrayList<>();
        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        for (Reserva reserva : todasReservas) {
            if (reserva.getFecha().equals(fechaHoy)) {
                reservasHoy.add(reserva);
            }
        }

        return reservasHoy;
    }
}
