package com.example.clubdetenis.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;  // <-- Import del Toolbar
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdetenis.Adapter.UsuariosAdapter;
import com.example.clubdetenis.R;
import com.example.clubdetenis.UsuarioResponse;
import com.example.clubdetenis.Utils.PreferenceManager;
import com.example.clubdetenis.api.ApiClient;
import com.example.clubdetenis.api.ApiService;
import com.example.clubdetenis.models.Usuario;
import com.example.clubdetenis.models.UsuarioRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuariosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerViewUsuarios;
    private Button btnAñadirUsuario;
    private EditText etNombre, etEmail, etPassword, etTelefono;
    private Spinner etPerfil;
    private SearchView txtBuscar;

    private ApiService apiService;
    private UsuariosAdapter usuarioAdapter;
    private PreferenceManager preferenceManager;

    private Toolbar toolbar;  // <-- Definimos el Toolbar

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        // Inicializar preferenceManager para obtener usuario logueado
        preferenceManager = new PreferenceManager(this);

        // Inicializar toolbar y configurarlo como ActionBar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        apiService = ApiClient.getClient().create(ApiService.class);

        recyclerViewUsuarios = findViewById(R.id.recyclerUsuarios);
        btnAñadirUsuario = findViewById(R.id.btnAñadirUsuario);
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etTelefono = findViewById(R.id.etTelefono);
        etPerfil = findViewById(R.id.spinnerPerfil);
        txtBuscar = findViewById(R.id.searchView);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.perfiles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etPerfil.setAdapter(adapter);

        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(this));
        usuarioAdapter = new UsuariosAdapter(this, new ArrayList<>());
        recyclerViewUsuarios.setAdapter(usuarioAdapter);

        loadUsuarios();

        btnAñadirUsuario.setOnClickListener(v -> {
            boolean valido = true;

            String nombre = etNombre.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();
            String perfil = etPerfil.getSelectedItem().toString().trim();

            if (nombre.isEmpty()) {
                etNombre.setError("Este campo es obligatorio");
                valido = false;
            }
            if (email.isEmpty()) {
                etEmail.setError("Este campo es obligatorio");
                valido = false;
            }
            if (password.isEmpty()) {
                etPassword.setError("Este campo es obligatorio");
                valido = false;
            }
            if (telefono.isEmpty()) {
                etTelefono.setError("Este campo es obligatorio");
                valido = false;
            }

            if (valido) {
                int esSocio = 1; // O cambia según tu lógica
                addUsuario(nombre, email, password, telefono, esSocio, perfil);
            } else {
                Toast.makeText(this, "Por favor, complete los campos marcados", Toast.LENGTH_SHORT).show();
            }
        });

        txtBuscar.setOnQueryTextListener(this);
    }

    private void loadUsuarios() {
        apiService.getUsuarios().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    usuarioAdapter.setUsuarios(response.body());
                } else {
                    Toast.makeText(UsuariosActivity.this, "No se pudieron cargar los usuarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(UsuariosActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addUsuario(String nombre, String email, String password, String telefono, int esSocio, String perfil) {
        UsuarioRequest usuarioRequest = new UsuarioRequest(nombre, email, password, telefono, esSocio, perfil);

        apiService.createUsuario(usuarioRequest).enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(UsuariosActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (response.body().isSuccess()) {
                        loadUsuarios();
                    }
                }
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                Toast.makeText(UsuariosActivity.this, "Error al añadir usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;  // No hacemos nada al enviar
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        usuarioAdapter.filtrado(newText);
        return false;
    }

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
            startActivity(new Intent(this, ReservasActivity.class));
            return true;
        } else if (id == R.id.menu_usuarios) {
            if (loggedUser != null && "Administrador".equals(loggedUser.getPerfil())) {
                Toast.makeText(this, "Ya estás en Usuarios", Toast.LENGTH_SHORT).show();
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
