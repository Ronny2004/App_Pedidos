package com.example.app_pedidos.model;

import java.sql.Timestamp;

public class Pedido {

    private int id;
    private int idUsuario; // Relación con el usuario
    private double total;
    private String estado; // 'pendiente', 'preparando', etc.
    private Timestamp fechaCreacion;
    private Timestamp fechaActualizacion;

    // Constructor
    public Pedido(int idUsuario, double total, String estado) {
        this.idUsuario = idUsuario;
        this.total = total;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Timestamp fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
