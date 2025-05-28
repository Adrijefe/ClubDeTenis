package com.example.clubdetenis;

import com.example.clubdetenis.models.Pista;

import java.util.List;

public class PistaResponse {
    private List<Pista> pistas;
    private boolean success;

    public List<Pista> getPistas() {
        return pistas;
    }

    public void setPistas(List<Pista> pistas) {
        this.pistas = pistas;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
