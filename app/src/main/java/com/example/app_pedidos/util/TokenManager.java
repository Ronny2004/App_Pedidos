package com.example.app_pedidos.util;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "auth_token";

    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString("token", null);
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
