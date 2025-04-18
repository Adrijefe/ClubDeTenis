package com.example.clubdetenis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


import com.example.clubdetenis.Utils.PreferenceManager;
import com.example.clubdetenis.activities.LoginActivity;
import com.example.clubdetenis.activities.PistasActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnPistas, btnReservas, btnLogout;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(this);
        if(!preferenceManager.isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        btnPistas = findViewById(R.id.btnPistas);
        btnReservas = findViewById(R.id.btnReservas);
        btnLogout = findViewById(R.id.btnLogout);

        btnPistas.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PistasActivity.class)));
        //btnReservas.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ReservasActivity.class)));
        btnLogout.setOnClickListener(v -> {
            preferenceManager.clear();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }
}