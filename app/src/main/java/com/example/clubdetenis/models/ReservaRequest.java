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

    // Estos campos nuevos para que el backend valide el admin que crea la reserva
    @SerializedName("usuarioIdSolicitante")
    private Integer usuarioIdSolicitante;  // Integer para que pueda ser null

    @SerializedName("perfil")
    private String perfil;

    public ReservaRequest(int usuarioId, int pistaId, String fecha, String horaInicio, String horaFin) {
        this.usuarioId = usuarioId;
        this.pistaId = pistaId;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = "confirmado";
        this.fecha = fecha;
    }

    // Getters y setters de siempre...

    public Integer getUsuarioIdSolicitante() {
        return usuarioIdSolicitante;
    }

    public void setUsuarioIdSolicitante(Integer usuarioIdSolicitante) {
        this.usuarioIdSolicitante = usuarioIdSolicitante;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

}
