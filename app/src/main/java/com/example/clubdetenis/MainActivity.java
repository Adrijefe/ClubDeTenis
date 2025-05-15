package com.example.clubdetenis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdetenis.Adapter.ReservasAdapter;
import com.example.clubdetenis.Utils.PreferenceManager;
import com.example.clubdetenis.activities.CrearReservaActivity;
import com.example.clubdetenis.activities.LoginActivity;
import com.example.clubdetenis.activities.PistasActivity;
import com.example.clubdetenis.activities.ReservasActivity;
import com.example.clubdetenis.activities.UsuariosActivity;
import com.example.clubdetenis.api.ApiClient;
import com.example.clubdetenis.api.ApiService;
import com.example.clubdetenis.models.Reserva;
import com.example.clubdetenis.models.Usuario;
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
    private Button btnPistas, btnReservas, btnReservar, btnUsuarios;
    private PreferenceManager preferenceManager;

    private RecyclerView recyclerView;
    private ReservasAdapter adapter;
    private List<Reserva> reservaList = new ArrayList<>();

    private TextView tvReservasHoy;  // <-- Añadido

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(this);
        if (!preferenceManager.isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnPistas = findViewById(R.id.btnPistas);
        btnReservas = findViewById(R.id.btnReservas);
        btnReservar = findViewById(R.id.btnNuevaReserva);
        btnUsuarios = findViewById(R.id.btnUsuarios);

        btnPistas.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PistasActivity.class)));
        btnReservas.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ReservasActivity.class)));
        btnReservar.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CrearReservaActivity.class)));
        btnUsuarios.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UsuariosActivity.class)));

        Usuario loggedUser = preferenceManager.getUser();
        if (loggedUser != null && "Administrador".equals(loggedUser.getPerfil())) {
            btnUsuarios.setVisibility(android.view.View.VISIBLE);
        } else {
            btnUsuarios.setVisibility(android.view.View.GONE);
        }

        recyclerView = findViewById(R.id.reservasListado);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReservasAdapter(this, reservaList);
        recyclerView.setAdapter(adapter);

        // Referencia el TextView y actualiza la fecha aquí
        tvReservasHoy = findViewById(R.id.tvReservasHoy);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaActual = sdf.format(new Date());
        tvReservasHoy.setText("Reservas Hoy - " + fechaActual);

        loadProximasReservas();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

                        List<Reserva> reservasHoy = filtrarReservasHoy(todasReservas);

                        if (reservasHoy.isEmpty()) {
                            Toast.makeText(MainActivity.this, "No tienes reservas para hoy.", Toast.LENGTH_SHORT).show();
                        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            preferenceManager.clear();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
