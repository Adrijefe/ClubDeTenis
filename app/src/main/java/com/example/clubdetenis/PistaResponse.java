package com.example.clubdetenis;// PistasResponse.java


import com.example.clubdetenis.models.Pista;

import java.util.List;

public class PistaResponse {
    private boolean success;  // Indica si la operación fue exitosa
    private List<Pista> pistas;  // La lista de pistas

    // Getters y setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Pista> getPistas() {
        return pistas;
    }

    public void setPistas(List<Pista> pistas) {
        this.pistas = pistas;
    }
}
