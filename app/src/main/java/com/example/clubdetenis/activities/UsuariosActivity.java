package com.example.clubdetenis.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

public class UsuariosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsuarios;
    private Button btnAñadirUsuario, btnEliminarUsuario;
    private EditText etNombre, etEmail, etPassword, etTelefono;
    private Spinner etPerfil;
    private ApiService apiService;
    private List<Usuario> usuariosList = new ArrayList<>();
    private UsuariosAdapter usuarioAdapter;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        // Inicializar Retrofit
        apiService = ApiClient.getClient().create(ApiService.class);

        // Inicializar vistas
        recyclerViewUsuarios = findViewById(R.id.recyclerUsuarios);
        btnAñadirUsuario = findViewById(R.id.btnAñadirUsuario);
        btnEliminarUsuario = findViewById(R.id.btnEliminarUsuario);
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);  // Campo para la contraseña
        etTelefono = findViewById(R.id.etTelefono);  // Campo para el teléfono
        etPerfil = findViewById(R.id.spinnerPerfil);  // Spinner para el perfil

        // Configurar el Spinner con opciones de perfil
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.perfiles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etPerfil.setAdapter(adapter);

        // Configurar RecyclerView
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(this));
        usuarioAdapter = new UsuariosAdapter(this, usuariosList);
        recyclerViewUsuarios.setAdapter(usuarioAdapter);

        // Cargar usuarios al iniciar la actividad
        loadUsuarios();

        // Acción del botón Añadir Usuario
        btnAñadirUsuario.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();
            String perfil = etPerfil.getSelectedItem().toString().trim();  // Obtener perfil desde Spinner
            boolean esSocio = true; // Cambiar según sea necesario

            // Validación básica de campos
            if (!nombre.isEmpty() && !email.isEmpty() && !password.isEmpty() && !telefono.isEmpty()) {
                addUsuario(nombre, email, password, telefono, esSocio ? 1 : 0, perfil);
            } else {
                Toast.makeText(UsuariosActivity.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            }
        });

        // Acción del botón Eliminar Usuario
        btnEliminarUsuario.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim(); // Agregar nombre en la validación
            String email = etEmail.getText().toString().trim();
            if (!nombre.isEmpty() && !email.isEmpty()) {
                deleteUsuario(nombre, email); // Método actualizado para eliminar por nombre y correo
            } else {
                Toast.makeText(UsuariosActivity.this, "Por favor, ingrese nombre y correo del usuario a eliminar.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUsuarios() {
        apiService.getUsuarios().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    usuariosList.clear();
                    usuariosList.addAll(response.body());
                    usuarioAdapter.notifyDataSetChanged();
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

    // Método para añadir un usuario
    private void addUsuario(String nombre, String email, String password, String telefono, int esSocio, String perfil) {
        // Crear el objeto UsuarioRequest
        UsuarioRequest usuarioRequest = new UsuarioRequest(nombre, email, password, telefono, esSocio, perfil);

        apiService.createUsuario(usuarioRequest).enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UsuarioResponse usuarioResponse = response.body();
                    Toast.makeText(UsuariosActivity.this, usuarioResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if (usuarioResponse.isSuccess()) {
                        loadUsuarios(); // Recargar usuarios después de añadir uno nuevo
                    }
                }
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                Toast.makeText(UsuariosActivity.this, "Error al añadir usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteUsuario(String nombre, String email) {
        apiService.eliminarUsuario(nombre, email).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UsuariosActivity.this, "Usuario eliminado con éxito", Toast.LENGTH_SHORT).show();
                    loadUsuarios(); // Recargar la lista de usuarios después de eliminar
                } else {
                    // Si el backend devuelve 404 o 400, mostramos mensaje personalizado
                    if (response.code() == 404) {
                        Toast.makeText(UsuariosActivity.this, "Usuario no encontrado. Verifique el nombre y el correo.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(UsuariosActivity.this, "No se pudo eliminar el usuario. Intente nuevamente.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UsuariosActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
