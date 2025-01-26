package com.example.app_pedidos.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Producto implements Parcelable {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String tipo; // Valores: 'plato', 'bebida', 'postre', 'adicional'
    private String estado; // Valores: 'disponible', 'agotado', 'pronto'

    // Constructor
    public Producto(int id, String nombre, String descripcion, double precio, String tipo, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipo = tipo;
        this.estado = estado;
    }

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Métodos obligatorios de la interfaz Parcelable

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeDouble(precio);
        dest.writeString(tipo);
        dest.writeString(estado);
    }

    // Constructor protegido de Parcelable
    protected Producto(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
        precio = in.readDouble();
        tipo = in.readString();
        estado = in.readString();
    }

    // Creador necesario para Parcelable
    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel in) {
            return new Producto(in);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
