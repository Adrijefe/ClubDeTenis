package com.example.clubdetenis.models;

public class ReservaRequest {
    private int usuario_id;
    private int pista_id;
    private String fecha;
    private String hora_inicio;
    private String hora_fin;

    public ReservaRequest(int usuario_id, int pista_id, String fecha, String hora_inicio, String hora_fin) {
        this.usuario_id = usuario_id;
        this.pista_id = pista_id;
        this.fecha = fecha;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
    }

    // Getters y setters


    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public int getPista_id() {
        return pista_id;
    }

    public void setPista_id(int pista_id) {
        this.pista_id = pista_id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(String hora_fin) {
        this.hora_fin = hora_fin;
    }
}
