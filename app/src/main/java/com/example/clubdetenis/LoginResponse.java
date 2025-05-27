package com.example.clubdetenis;

import com.example.clubdetenis.models.Usuario;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private boolean success; // Indica si la operación fue exitosa
    private String message;    // Mensaje  (éxito, error, detalles, etc.)

    @SerializedName("user")
    private Usuario user;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Usuario getUser() {
        return user;
    }
}
