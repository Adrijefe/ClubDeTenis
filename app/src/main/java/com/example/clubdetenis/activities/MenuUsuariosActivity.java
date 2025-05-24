package com.example.clubdetenis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.clubdetenis.R;
import com.example.clubdetenis.Utils.PreferenceManager;
import com.example.clubdetenis.models.Usuario;

public class MenuUsuariosActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_socios);

        // Inicializar toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicializar preferenceManager para permisos/menu
        preferenceManager = new PreferenceManager(this);

        Button btnListadoSocios = findViewById(R.id.btnListadoSocios);
        Button btnGestionUsuarios = findViewById(R.id.btnGestionUsuarios);

        btnListadoSocios.setOnClickListener(v ->
                startActivity(new Intent(MenuUsuariosActivity.this, ListadoSociosActivity.class)));

        btnGestionUsuarios.setOnClickListener(v ->
                startActivity(new Intent(MenuUsuariosActivity.this, UsuariosActivity.class)));
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
