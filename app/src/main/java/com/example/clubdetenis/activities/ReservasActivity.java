package com.example.clubdetenis.activities;

// Imports necesarios para la actividad
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdetenis.Adapter.ReservasAdapter;
import com.example.clubdetenis.R;
import com.example.clubdetenis.Utils.PreferenceManager;
import com.example.clubdetenis.api.ApiClient;
import com.example.clubdetenis.api.ApiService;
import com.example.clubdetenis.models.Reserva;
import com.example.clubdetenis.models.Usuario;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservasActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Se declaran los elementos que se usarán en la vista: lista, adaptador, búsqueda y gestión de sesión
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

        // Se inicializa la toolbar como barra de acciones
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Se obtiene el ID del usuario actualmente logueado desde las preferencias
        preferenceManager = new PreferenceManager(this);
        int usuarioId = preferenceManager.getUser().getId();

        // Se configura el RecyclerView con el layoutl y se asigna el adaptador
        recyclerView = findViewById(R.id.recyclerViewReservas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReservasAdapter(this, reservaList);
        recyclerView.setAdapter(adapter);

        // Se configura el SearchView para permitir el filtrado de la lista
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);

        // Se llama al método que obtiene las reservas desde la API
        loadReservas(usuarioId);
    }

    // Método para recuperar las reservas desde la API según el perfil del usuario
    private void loadReservas(int usuarioId) {
        String perfil = preferenceManager.getUser().getPerfil();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Reserva>> call = apiService.getReservasPorPerfil(true, usuarioId, perfil);

        call.enqueue(new Callback<List<Reserva>>() {
            @Override
            public void onResponse(Call<List<Reserva>> call, Response<List<Reserva>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Reserva> reservas = response.body();
                    adapter.setReserva(reservas);
                } else {
                    Toast.makeText(ReservasActivity.this, "Error al cargar reservas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Reserva>> call, Throwable t) {
                Log.e("ReservasActivity", "Error en la conexión", t);
                Toast.makeText(ReservasActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Implementación de la búsqueda al enviar el texto no se hace nada especial
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    // Implementación de la búsqueda al cambiar el texto, se filtran las reservas
    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filtrado(newText);
        return false;
    }

    // Se crea el menú de opciones (Toolbar) y se oculta o muestra según el perfil del usuario
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        MenuItem menuUsuarios = menu.findItem(R.id.menu_usuarios);
        Usuario loggedUser = preferenceManager.getUser();

        if (menuUsuarios != null) {
            if (loggedUser != null && "Administrador".equals(loggedUser.getPerfil())) {
                menuUsuarios.setVisible(true);
            } else {
                menuUsuarios.setVisible(false);
            }
        }

        return true;
    }

    // Se gestionan las acciones al pulsar elementos del menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Usuario loggedUser = preferenceManager.getUser();


        if (id == R.id.menu_crear_reserva) {
            startActivity(new Intent(this, CrearReservaActivity.class));
            return true;

        } else if (id == R.id.menu_pistas) {
            startActivity(new Intent(this, PistasActivity.class));
            return true;

        } else if (id == R.id.menu_reservas) {
            Toast.makeText(this, "Ya estás en Reservas", Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(this, LoginActivity.class)); // Se vuelve al login
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
