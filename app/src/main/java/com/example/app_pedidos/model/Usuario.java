package com.example.app_pedidos.model;

public class Usuario {
    private int id;
    private String nombre;
    private String correo;
    private String rol;
    private String estado;
    private String fecha_creacion;

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaCreacion() {
        return fecha_creacion;
    }

    public void setFechaCreacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
}

