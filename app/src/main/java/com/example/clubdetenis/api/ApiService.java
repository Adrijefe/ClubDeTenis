package com.example.clubdetenis.api;

import com.example.clubdetenis.DTO.DTOLogin;
import com.example.clubdetenis.LoginResponse;
import com.example.clubdetenis.PistaResponse;
import com.example.clubdetenis.UsuarioResponse;
import com.example.clubdetenis.models.Pista;
import com.example.clubdetenis.models.Reserva;
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
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {

    // Cambié "login.php" por la ruta del controlador Spring /api/login
    @POST("api/login")
    Call<LoginResponse> login(@Body DTOLogin loginRequest);

    // Cambié "pista.php" por /api/pistas
    @GET("api/pistas")
    Call<PistaResponse> getPistas();

    @GET("api/pistas/disponibles")
    Call<PistaResponse> getPistasDisponibles();


    // Cambié "reservas.php" por /api/reservas
    @POST("api/reservas")
    Call<JsonObject> crearReserva(@Body ReservaRequest reservaRequest);


    @GET("api/reservas/misreservas")
    Call<List<Reserva>> getReservasPorPerfil(
            @Query("misreservas") boolean misReservas,
            @Query("usuarioId") int usuarioId,
            @Query("perfil") String perfil
    );
    @GET("api/reservas/hoy")
    Call<List<Reserva>> getReservasDeHoy();



    @GET("api/reservas/disponibles")
    Call<JsonObject> getHorasDisponibles(@Query("fecha") String fecha, @Query("pistaId") int pistaId);

    @DELETE("api/reservas")
    Call<Void> eliminarReserva(@Query("id") int reservaId);

    // Métodos para usuarios
    @GET("api/usuarios")
    Call<List<Usuario>> getUsuarios();

    @POST("api/usuarios")
    Call<UsuarioResponse> createUsuario(@Body UsuarioRequest usuario);

    @DELETE("api/usuarios")
    Call<Void> eliminarUsuario(@Query("nombre") String nombre, @Query("email") String email);

}
