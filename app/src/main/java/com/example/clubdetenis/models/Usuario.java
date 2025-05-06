package com.example.clubdetenis.models;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private int esSocio;  // Cambiar a int, ya que el servidor envía un número (1 o 0)

    public Usuario(int id, String nombre, String email, int esSocio) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.esSocio = esSocio;
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public int getEsSocio() { return esSocio; }  // Cambiado a int
}
