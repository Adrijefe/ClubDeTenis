package com.example.clubdetenis.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdetenis.Adapter.UsuariosAdapter;
import com.example.clubdetenis.R;
import com.example.clubdetenis.UsuarioResponse;
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

    // Declaramos los componentes
    private RecyclerView recyclerViewUsuarios;
    private Button btnAñadirUsuario;
    private EditText etNombre, etEmail, etPassword, etTelefono;
    private Spinner etPerfil;
    private SearchView txtBuscar;

    private ApiService apiService;
    private UsuariosAdapter usuarioAdapter;

    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        // Configuración del Toolbar con botón de volver
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestión de Usuarios");
        }

        // Inicialización del servicio de API
        apiService = ApiClient.getClient().create(ApiService.class);

        // Referencias a elementos del layout
        recyclerViewUsuarios = findViewById(R.id.recyclerUsuarios);
        btnAñadirUsuario = findViewById(R.id.btnAñadirUsuario);
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etTelefono = findViewById(R.id.etTelefono);
        etPerfil = findViewById(R.id.spinnerPerfil);
        txtBuscar = findViewById(R.id.searchView);

        // Configuración del spinner de perfil (Administrador/Socio)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.perfiles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etPerfil.setAdapter(adapter);

        // Configuración del RecyclerView
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(this));
        // El adaptador muestra botón de eliminar (true) y oculta teléfono (false)
        // para que no aparezca en la lista de la gestion de usuarios
        usuarioAdapter = new UsuariosAdapter(this, new ArrayList<>(), true, false);
        recyclerViewUsuarios.setAdapter(usuarioAdapter);

        // Carga de usuarios desde la API
        loadUsuarios();

        // Acción al hacer clic en Añadir Usuario
        btnAñadirUsuario.setOnClickListener(v -> {
            Toast.makeText(this, "Click detectado", Toast.LENGTH_SHORT).show();

            boolean valido = true;
            // Obtención de valores ingresados
            String nombre = etNombre.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();
            String perfil = etPerfil.getSelectedItem().toString().trim();

            // Validación de campos obligatorios
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
                int esSocio = 1; // Puedes ajustar esto según la lógica de tu app
                addUsuario(nombre, email, password, telefono, esSocio, perfil);
            } else {
                Toast.makeText(this, "Por favor, complete los campos marcados", Toast.LENGTH_SHORT).show();
            }
        });

        // Escucha cambios en el campo de búsqueda
        txtBuscar.setOnQueryTextListener(this);
    }

    // Cargar usuarios desde la API y mostrarlos en el RecyclerView
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

    // Lógica para enviar un nuevo usuario a la API
    private void addUsuario(String nombre, String email, String password, String telefono, int esSocio, String perfil) {
        UsuarioRequest usuarioRequest = new UsuarioRequest(nombre, email, password, telefono, esSocio, perfil);

        // Mostrar el JSON que se enviará (si deseas puedes usar Gson para convertirlo)
        Toast.makeText(this, "Enviando usuario: " + nombre, Toast.LENGTH_SHORT).show();

        apiService.createUsuario(usuarioRequest).enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean success = response.body().isSuccess();
                    String message = response.body().getMessage();

                    Log.d("KAKA", "no funciona");
                    if (success) {
                        loadUsuarios(); // Recargar lista si fue exitoso
                    } else {
                        Toast.makeText(UsuariosActivity.this, "Error desde servidor: " + message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Respuesta no exitosa: error del lado del servidor
                    String error = "Error " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            error += ": " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(UsuariosActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                Toast.makeText(UsuariosActivity.this, "Fallo de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    // Métodos del SearchView para filtrar resultados
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false; // No hacemos nada al enviar búsqueda
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        usuarioAdapter.filtrado(newText); // Filtrar en tiempo real
        return false;
    }

    // Flecha "volver" del Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Cierra la actividad
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
