package com.example.clubdetenis.DTO;
// almacenamos la información del inicio de sesión y lo cogemos a traves de retrofit
public class DTOLogin {
    private String email;
    private String password;

    // Constructor
    public DTOLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters y Setters
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
}
