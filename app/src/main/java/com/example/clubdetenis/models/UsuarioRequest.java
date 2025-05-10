package com.example.clubdetenis.models;

public class UsuarioRequest {
    private String nombre;
    private String email;
    private String password;
    private String telefono;
    private int esSocio;
    private String perfil;

    // Constructor, getters y setters
    public UsuarioRequest(String nombre, String email, String password, String telefono, int esSocio, String perfil) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.esSocio = esSocio;
        this.perfil = perfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getEsSocio() {
        return esSocio;
    }

    public void setEsSocio(int esSocio) {
        this.esSocio = esSocio;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}
