package com.example.app_pedidos.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("correo") // Mapeo del campo 'correo' en el JSON
    private String email;

    @SerializedName("contrasena") // Mapeo del campo 'contraseña' en el JSON
    private String password;

    // Constructor, getters y setters
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
