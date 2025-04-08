package com.example.clubdetenis.api;
import com.example.clubdetenis.DTO.DTOLogin;
import com.example.clubdetenis.models.Pista;
import com.example.clubdetenis.models.Reserva;
import com.example.clubdetenis.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
public interface ApiService {
    @POST("login.php")
    Call<Usuario> login(@Body DTOLogin loginRequest);

    @GET("pistas.php")
    Call<List<Pista>> getPistas();

    @GET("reservas.php")
    Call<List<Reserva>> getReservas(@Query("usuario_id") int usuarioId);

    @POST("reservas.php")
    Call<Void> createReserva(@Body Reserva reserva);


}
