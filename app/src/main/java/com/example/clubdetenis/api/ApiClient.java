package com.example.clubdetenis.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static String email;
    public static String password;
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        if (retrofit == null) {
            // Configura Gson para que acepte JSON malformado
            Gson gson = new GsonBuilder()
                    .setLenient() // Permitir JSON malformado
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://demo-j2tl.onrender.com/")
                    //.baseUrl("http://10.204.223.87:10000/")
                    .addConverterFactory(GsonConverterFactory.create(gson)) // Usa Gson con la configuración lenient
                    .build();
        }
        return retrofit;
    }
}
