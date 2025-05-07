package com.example.clubdetenis.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Configura Gson para que acepte JSON malformado
            Gson gson = new GsonBuilder()
                    .setLenient() // Permitir JSON malformado
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://172.20.10.8//Miproyecto/")
                    .addConverterFactory(GsonConverterFactory.create(gson)) // Usa Gson con la configuración lenient
                    .build();
        }
        return retrofit;
    }
}
