package com.example.clubdetenis.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdetenis.Adapter.ReservasAdapter;
import com.example.clubdetenis.R;
import com.example.clubdetenis.Utils.PreferenceManager;
import com.example.clubdetenis.api.ApiClient;
import com.example.clubdetenis.api.ApiService;
import com.example.clubdetenis.models.Reserva;
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

public class ReservasActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    private ReservasAdapter adapter;
    private List<Reserva> reservaList = new ArrayList<>();
    private PreferenceManager preferenceManager;
    private SearchView searchView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        preferenceManager = new PreferenceManager(this);
        int usuarioId = preferenceManager.getUser().getId();

        recyclerView = findViewById(R.id.recyclerViewReservas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ReservasAdapter(this, reservaList);
        recyclerView.setAdapter(adapter);

        searchView = findViewById(R.id.searchView); // Asegúrate que exista en el layout
        searchView.setOnQueryTextListener(this);

        loadReservas(usuarioId);
    }

    private void loadReservas(int usuarioId) {
        String perfil = preferenceManager.getUser().getPerfil();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<JsonObject> call = apiService.getReservasPorPerfil(true, usuarioId, perfil);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JsonObject json = response.body();
                        JsonArray reservasJson = json.getAsJsonArray("reservas");

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Reserva>>() {}.getType();
                        List<Reserva> reservas = gson.fromJson(reservasJson, listType);

                        adapter.setReserva(reservas);

                    } catch (Exception e) {
                        Log.e("ReservasActivity", "Error al parsear JSON", e);
                        Toast.makeText(ReservasActivity.this, "Error al procesar datos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ReservasActivity.this, "Error al cargar reservas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("ReservasActivity", "Error en la conexión", t);
                Toast.makeText(ReservasActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;  // No hacemos nada al enviar
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filtrado(newText);
        return false;
    }
}
