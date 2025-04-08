package com.example.clubdetenis.Utils;

import android.content.Context;
import android.content.SharedPreferences;


import com.example.clubdetenis.models.Usuario;
import com.google.gson.Gson;

public class PreferenceManager {
    private static final String PREF_NAME = "ClubTenisPref";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER = "user";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public PreferenceManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveUser(Usuario user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER, userJson);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public Usuario getUser() {
        Gson gson = new Gson();
        String userJson = pref.getString(KEY_USER, "");
        return gson.fromJson(userJson, Usuario.class);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, true);
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }
}