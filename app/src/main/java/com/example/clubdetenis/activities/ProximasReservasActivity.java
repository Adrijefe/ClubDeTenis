package com.example.clubdetenis.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProximasReservasActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ReservasAdapter adapter;
    private List<Reserva> reservaList = new ArrayList<>();
    private PreferenceManager preferenceManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas); // puedes usar el mismo layout

        preferenceManager = new PreferenceManager(this);
        int usuarioId = preferenceManager.getUser().getId();

        recyclerView = findViewById(R.id.recyclerViewReservas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReservasAdapter(this, reservaList);
        recyclerView.setAdapter(adapter);

        loadProximasReservas(usuarioId);
    }

    private void loadProximasReservas(int usuarioId) {
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
                        List<Reserva> reservas = gson.fromJson(reservasJson, listType);

                        List<Reserva> reservasFiltradas = new ArrayList<>();
                        for (Reserva r : reservas) {
                            if (cumpleCondicion(r)) {
                                reservasFiltradas.add(r);
                            }
                        }

                        reservaList.clear();
                        reservaList.addAll(reservasFiltradas);
                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.e("ProximasReservasActivity", "Error al parsear JSON", e);
                        Toast.makeText(ProximasReservasActivity.this, "Error al procesar datos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProximasReservasActivity.this, "Error al cargar reservas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("ProximasReservasActivity", "Error en la conexión", t);
                Toast.makeText(ProximasReservasActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean cumpleCondicion(Reserva reserva) {
        try {
            String fechaReserva = reserva.getFecha();        // ejemplo: "2025-05-08"
            String horaFin = reserva.getHoraFin();           // ejemplo: "18:00"

            SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String fechaHoy = sdfFecha.format(new Date());

            if (!fechaReserva.equals(fechaHoy)) {
                return false;
            }

            SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String horaActualStr = sdfHora.format(new Date());
            Date horaActual = sdfHora.parse(horaActualStr);
            Date horaFinReserva = sdfHora.parse(horaFin);

            return horaFinReserva.after(horaActual);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
