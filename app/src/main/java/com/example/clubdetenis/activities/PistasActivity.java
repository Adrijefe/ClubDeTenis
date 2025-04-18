// PistasActivity.java
package com.example.clubdetenis.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdetenis.Adapter.PistasAdapter;
import com.example.clubdetenis.PistaResponse;
import com.example.clubdetenis.R;
import com.example.clubdetenis.api.ApiClient;
import com.example.clubdetenis.api.ApiService;
import com.example.clubdetenis.models.Pista;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PistasActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PistasAdapter adapter;
    private List<Pista> pistaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pistas);

        recyclerView = findViewById(R.id.recyclerViewPistas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PistasAdapter(this, pistaList);
        recyclerView.setAdapter(adapter);

        loadPistas();
    }

    private void loadPistas() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<PistaResponse> call = apiService.getPistas();  // Usamos PistasResponse aquí

        call.enqueue(new Callback<PistaResponse>() {  // Cambiar a PistasResponse
            @Override
            public void onResponse(Call<PistaResponse> call, Response<PistaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {  // Verificar si la respuesta es exitosa
                        pistaList.clear();
                        pistaList.addAll(response.body().getPistas());  // Obtener la lista de pistas
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(PistasActivity.this, "Error al cargar pistas", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PistasActivity.this, "Error al cargar pistas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PistaResponse> call, Throwable t) {
                Toast.makeText(PistasActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
