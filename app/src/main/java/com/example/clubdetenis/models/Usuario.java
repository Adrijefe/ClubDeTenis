package com.example.clubdetenis.models;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private boolean esSocio;

    public Usuario(int id, String nombre, String email, boolean esSocio) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.esSocio = esSocio;
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public boolean isEsSocio() { return esSocio; }
}
