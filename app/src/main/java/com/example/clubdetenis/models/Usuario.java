package com.example.clubdetenis.models;

import com.google.gson.annotations.SerializedName;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String perfil;
    private String telefono;  // Nuevo campo teléfono

    // Constructor actualizado para aceptar perfil y teléfono
    public Usuario(int id, String nombre, String email, String perfil, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.perfil = perfil;
        this.telefono = telefono;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPerfil() { return perfil; }
    public String getTelefono() { return telefono; }  // Getter teléfono

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setPerfil(String perfil) { this.perfil = perfil; }
    public void setTelefono(String telefono) { this.telefono = telefono; }  // Setter teléfono
}
