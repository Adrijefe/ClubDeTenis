package com.example.clubdetenis.models;

import com.google.gson.annotations.SerializedName;

public class ReservaRequest {

    @SerializedName("usuario_id")
    private int usuarioId;

    @SerializedName("pista_id")
    private int pistaId;

    private String fecha;

    @SerializedName("hora_inicio")
    private String horaInicio;

    @SerializedName("hora_fin")
    private String horaFin;

    public ReservaRequest(int usuarioId, int pistaId, String fecha, String horaInicio, String horaFin) {
        this.usuarioId = usuarioId;
        this.pistaId = pistaId;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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
}
