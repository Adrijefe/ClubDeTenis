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

    @SerializedName("estado")  // Agregar el mapeo para el campo "estado"
    private String estado;

    @SerializedName("fecha")  // Mapear el campo "fecha" para el JSON
    private String fecha_reserva;

    // Constructor con los parámetros adecuados
    public ReservaRequest(int usuarioId, int pistaId, String fecha, String horaInicio, String horaFin) {
        this.usuarioId = usuarioId;
        this.pistaId = pistaId;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = "confirmado";  // Se puede mantener como un valor predeterminado
        this.fecha_reserva = fecha; // Asignar la fecha del parámetro al campo
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

    public String getFecha_reserva() {
        return fecha_reserva;
    }

    public void setFecha_reserva(String fecha_reserva) {
        this.fecha_reserva = fecha_reserva;
    }
}
