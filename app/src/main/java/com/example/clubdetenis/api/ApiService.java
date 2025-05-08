package com.example.clubdetenis.api;

import com.example.clubdetenis.DTO.DTOLogin;
import com.example.clubdetenis.LoginResponse;
import com.example.clubdetenis.PistaResponse;
import com.example.clubdetenis.models.ReservaRequest;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Path;

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

    // Nuevo método para obtener las reservas del día actual
    @GET("reservas.php")
    Call<JsonObject> getReservasHoy(@Query("reservasHoy") boolean reservasHoy);
}
