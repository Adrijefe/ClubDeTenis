package com.example.clubdetenis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdetenis.Adapter.PistasAdapter;
import com.example.clubdetenis.PistaResponse;
import com.example.clubdetenis.R;
import com.example.clubdetenis.api.ApiClient;
import com.example.clubdetenis.api.ApiService;
import com.example.clubdetenis.models.Pista;
import com.example.clubdetenis.Utils.PreferenceManager;
import com.example.clubdetenis.models.Usuario;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PistasActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PistasAdapter adapter;
    private List<Pista> pistaList = new ArrayList<>();
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pistas);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferenceManager = new PreferenceManager(this);

        recyclerView = findViewById(R.id.recyclerViewPistas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Establece el layout para mostrar las pistas en lista

        adapter = new PistasAdapter(this, pistaList);
        recyclerView.setAdapter(adapter);
        // Asigna el adaptador al RecyclerView para mostrar datos

        loadPistas();
        // Carga las pistas desde la API para mostrarlas en la lista
    }

    private void loadPistas() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<PistaResponse> call = apiService.getPistas();
        // Llama al endpoint para obtener la lista de pistas

        call.enqueue(new Callback<PistaResponse>() {
            @Override
            public void onResponse(Call<PistaResponse> call, Response<PistaResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    pistaList.clear();
                    pistaList.addAll(response.body().getPistas());
                    adapter.notifyDataSetChanged();
                    // Actualiza el adaptador para que muestre los datos recién recibidos
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        MenuItem menuUsuarios = menu.findItem(R.id.menu_usuarios);

        Usuario loggedUser = preferenceManager.getUser(); // Obtiene usuario logueado para control de acceso

        if (menuUsuarios != null) {
            if (loggedUser != null && "Administrador".equals(loggedUser.getPerfil())) {
                menuUsuarios.setVisible(true);
                // Solo muestra el menú usuarios si el perfil es Administrador
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
            startActivity(new Intent(this, CrearReservaActivity.class));
            return true;
        } else if (id == R.id.menu_pistas) {
            Toast.makeText(this, "Ya estás en Pistas", Toast.LENGTH_SHORT).show();
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
            // Limpia datos guardados localmente para cerrar sesión
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // Cierra esta actividad para evitar volver atrás
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
