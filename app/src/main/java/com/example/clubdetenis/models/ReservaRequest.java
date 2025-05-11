package com.example.clubdetenis.models;

import com.google.gson.annotations.SerializedName;

public class ReservaRequest {

    @SerializedName("usuarioId")
    private int usuarioId;

    @SerializedName("pistaId")
    private int pistaId;

    @SerializedName("horaInicio")
    private String horaInicio;

    @SerializedName("horaFin")
    private String horaFin;

    @SerializedName("estado")
    private String estado;

    @SerializedName("fecha")
    private String fecha;

    // Constructor con los parámetros adecuados
    public ReservaRequest(int usuarioId, int pistaId, String fecha, String horaInicio, String horaFin) {
        this.usuarioId = usuarioId;
        this.pistaId = pistaId;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = "confirmado";  // Se puede mantener como un valor predeterminado
        this.fecha = fecha; // Asignar la fecha del parámetro al campo
    }

    // Getters y setters
    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getPistaId() {
        return pistaId;
    }

    public void setPistaId(int pistaId) {
        this.pistaId = pistaId;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
