package com.example.clubdetenis.models;

public class Reserva {
    private int id;
    private int usuarioId;
    private int pistaId;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private String estado;
    private String pistaNombre;

    public Reserva(int id, int usuarioId, int pistaId, String fecha, String horaInicio, String horaFin, String estado, String pistaNombre) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.pistaId = pistaId;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
        this.pistaNombre = pistaNombre;
    }

    // Getters y Setters
    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public int getPistaId() { return pistaId; }
    public String getFecha() { return fecha; }
    public String getHoraInicio() { return horaInicio; }
    public String getHoraFin() { return horaFin; }
    public String getEstado() { return estado; }
    public String getPistaNombre() { return pistaNombre; }
}
