package com.example.clubdetenis.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdetenis.Adapter.UsuariosAdapter;
import com.example.clubdetenis.R;
import com.example.clubdetenis.api.ApiClient;
import com.example.clubdetenis.api.ApiService;
import com.example.clubdetenis.models.Usuario;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListadoSociosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerViewSocios;
    private SearchView searchViewSocios;
    private UsuariosAdapter usuariosAdapter;
    private ApiService apiService;
    private Toolbar toolbarListadoSocios;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_socios);

        // Toolbar
        toolbarListadoSocios = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarListadoSocios);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerViewSocios = findViewById(R.id.recyclerUsuarios);
        searchViewSocios = findViewById(R.id.searchView);

        recyclerViewSocios.setLayoutManager(new LinearLayoutManager(this));
        // Aquí: botón eliminar oculto (false), mostrar teléfono visible (true)
        usuariosAdapter = new UsuariosAdapter(this, new ArrayList<>(), false, true);
        recyclerViewSocios.setAdapter(usuariosAdapter);

        apiService = ApiClient.getClient().create(ApiService.class);
        cargarSocios();

        searchViewSocios.setOnQueryTextListener(this);
    }

    private void cargarSocios() {
        apiService.getUsuarios().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Usuario> soloSocios = new ArrayList<>();
                    for (Usuario usuario : response.body()) {
                        if ("Socio".equalsIgnoreCase(usuario.getPerfil())) {
                            soloSocios.add(usuario);
                        }
                    }
                    usuariosAdapter.setUsuarios(soloSocios);
                } else {
                    Toast.makeText(ListadoSociosActivity.this, "Error al cargar los socios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(ListadoSociosActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        usuariosAdapter.filtrado(newText);
        return false;
    }

    // Flecha "volver"
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
