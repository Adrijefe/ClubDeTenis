package com.example.clubdetenis.models;

public class Pista {
    private int id;
    private String nombre;
    private String tipo;
    private String descripcion;
    private double precioHora;

    public Pista(int id, String nombre, String tipo, String descripcion, double precioHora) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.precioHora = precioHora;

    }

    // Getters y Setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public double getPrecioHora() { return precioHora; }
}
