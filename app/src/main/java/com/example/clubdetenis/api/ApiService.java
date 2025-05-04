package com.example.clubdetenis.api;
import com.example.clubdetenis.DTO.DTOLogin;
import com.example.clubdetenis.PistaResponse;
import com.example.clubdetenis.models.Reserva;
import com.example.clubdetenis.models.ReservaRequest;
import com.example.clubdetenis.models.Usuario;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
public interface ApiService {
    @POST("login.php")
    Call<Usuario> login(@Body DTOLogin loginRequest);

    @GET("pista.php")
    Call<PistaResponse> getPistas();

    // Para crear reservas
    @POST("reservas.php")
    Call<JsonObject> crearReserva(@Body ReservaRequest reservaRequest);

    // Para obtener reservas (ya lo tienes)
    @GET("reservas.php")
    Call<JsonObject> getReservas(@Query("usuario_id") int usuarioId);

    // Para obtener pistas disponibles
    @GET("pista.php")
    Call<JsonObject> getPistasDisponibles();


}
