package com.example.clubdetenis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clubdetenis.DTO.DTOLogin;
import com.example.clubdetenis.LoginResponse;
import com.example.clubdetenis.MainActivity;
import com.example.clubdetenis.R;
import com.example.clubdetenis.Utils.PreferenceManager;
import com.example.clubdetenis.api.ApiClient;
import com.example.clubdetenis.api.ApiService;

import com.example.clubdetenis.models.Usuario;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferenceManager = new PreferenceManager(this);

        // Verifica si ya hay un usuario logueadp si es asi lo redirige a MainActivity
        if (preferenceManager.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish(); // Evita volver a la pantalla de login
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Listener para el botón que ejecuta el método loginUser al ser presionado
        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validación  para que no haya campos vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        DTOLogin loginRequest = new DTOLogin(email, password);
        // Crea un objeto con los datos de login para enviar al servidor

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.login(loginRequest);
        // Llama al endpoint de login mediante Retrofit

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                // Se ejecuta cuando la respuesta HTTP es recibida correctamente
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess()) {
                        Usuario loggedUser = loginResponse.getUser();
                        // Loguea información para depuración
                        Log.d("LoginActivity", "Usuario ID recibido: " + loggedUser.getId());
                        Log.d("LoginActivity", "Perfil del usuario: " + loggedUser.getPerfil());

                        preferenceManager.saveUser(loggedUser);
                        // Guarda localmente los datos del usuario para mantener sesión

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish(); // Evita volver a login con botón "atrás"
                    } else {
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Maneja errores de respuesta del servidor, mostrando mensaje y logs
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No hay cuerpo de error";
                        Log.e("LoginActivity", "Error: " + errorBody);
                        Toast.makeText(LoginActivity.this, "Error de respuesta del servidor: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Se ejecuta si hay problemas de conexión o falla de la llamada
                Log.e("LoginActivity", "Error de conexión",t.getCause());
                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

