package com.example.clubdetenis.models;

public class Pista {
    private int id;
    private String nombre;
    private String tipo;
    private String descripcion;
    private String precioHora;
    private String imagen;

    public Pista() {
    }

    public Pista(int id, String nombre, String tipo, String descripcion, String precioHora, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.precioHora = precioHora;
        this.imagen = imagen;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecioHora() {
        return precioHora;
    }

    public void setPrecioHora(String precioHora) {
        this.precioHora = precioHora;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    // Método toString() para que el Spinner muestre el nombre
    @Override
    public String toString() {
        return nombre;  // El spinner solo mostrará el nombre de la pista
    }
}
