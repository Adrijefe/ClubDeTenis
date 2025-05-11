package com.example.clubdetenis.models;

import com.google.gson.annotations.SerializedName;

public class Usuario {
    private int id;
    private String nombre;
    private String email;

    private String perfil;  // Nuevo campo para almacenar el perfil del usuario

    // Constructor actualizado para aceptar perfil
    public Usuario(int id, String nombre, String email, String perfil) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.perfil = perfil;  // Asignamos el perfil directamente
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPerfil() { return perfil; }  // Obtener el perfil del usuario

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setPerfil(String perfil) { this.perfil = perfil; }  // Establecer el perfil del usuario
}
