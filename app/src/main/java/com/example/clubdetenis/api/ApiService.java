package com.example.clubdetenis.api;

import com.example.clubdetenis.DTO.DTOLogin;
import com.example.clubdetenis.LoginResponse;
import com.example.clubdetenis.PistaResponse;
import com.example.clubdetenis.UsuarioResponse;
import com.example.clubdetenis.models.ReservaRequest;
import com.example.clubdetenis.models.Usuario;

import com.example.clubdetenis.models.UsuarioRequest;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("login.php")
    Call<LoginResponse> login(@Body DTOLogin loginRequest);

    @GET("pista.php")
    Call<PistaResponse> getPistas();

    @POST("reservas.php")
    Call<JsonObject> crearReserva(@Body ReservaRequest reservaRequest);

    @GET("reservas.php")
    Call<JsonObject> getReservas(@Query("usuario_id") int usuarioId);

    @GET("pista.php")
    Call<JsonObject> getPistasDisponibles();

    @GET("reservas.php")
    Call<JsonObject> getHorasDisponibles(@Query("fecha") String fecha, @Query("pistaId") int pistaId);

    @DELETE("reservas.php")
    Call<Void> eliminarReserva(@Query("id") int reservaId);

    @GET("reservas.php")
    Call<JsonObject> getReservasHoy(@Query("reservasHoy") boolean reservasHoy);

    // Métodos para usuarios
    @GET("usuarios.php") // Obtener todos los usuarios
    Call<List<Usuario>> getUsuarios();

    @POST("usuarios.php") // Crear un nuevo usuario
    Call<UsuarioResponse> createUsuario(@Body UsuarioRequest usuario);

    @DELETE("usuarios.php")
    Call<Void> eliminarUsuario(@Query("nombre") String nombre, @Query("email") String email);

}
