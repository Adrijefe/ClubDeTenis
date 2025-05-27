package com.example.clubdetenis.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.clubdetenis.models.Usuario;
import com.google.gson.Gson;

// Clase encargada de gestionar las preferencias compartidas


public class PreferenceManager {

    // Constantes para el nombre del archivo y claves de los datos almacenados
    private static final String PREF_NAME = "ClubTenisPref";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER = "user";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    //Constructor que inicializa el acceso.

    public PreferenceManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }


     // Guarda los datos del usuario como JSON, y marca al usuario como logueado.

    public void saveUser(Usuario user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER, userJson);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }


    // Devuelve el objeto Usuario


    public Usuario getUser() {
        Gson gson = new Gson();
        String userJson = pref.getString(KEY_USER, "");
        return gson.fromJson(userJson, Usuario.class);
    }


    // Verifica si el usuario está logueado según este almacenada.

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Elimina todos los datos almacenados, lo usamos para cerrar sesión
    public void clear() {
        editor.clear();
        editor.apply();
    }
}
