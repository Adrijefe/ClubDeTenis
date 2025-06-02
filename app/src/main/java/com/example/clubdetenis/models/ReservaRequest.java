package com.example.clubdetenis.models;

import com.google.gson.annotations.SerializedName;
//  representa los datos que se envían para crear o manejar una reserva
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

    @SerializedName("perfil")
    private String perfil;

    public ReservaRequest(int usuarioId, int pistaId, String fecha, String horaInicio, String horaFin) {
        this.usuarioId = usuarioId;
        this.pistaId = pistaId;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = "confirmada";
        this.fecha = fecha;
    }


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

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

}
