package com.example.clubdetenis;

// Clase modelo para representar la respuesta de una operación relacionada con usuarios.

public class UsuarioResponse {

    // Indica si la operación fue exitosa
    private boolean success;

    // Mensaje  (éxito, error, detalles, etc.)
    private String message;

    // Constructor

    public UsuarioResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
