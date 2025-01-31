package com.example.app_pedidos.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.app_pedidos.model.Pedido;
import com.example.app_pedidos.model.Producto;
import com.example.app_pedidos.model.DetallePedido; // Asegúrate de tener la clase DetallePedido
import com.example.app_pedidos.model.LoginRequest;
import com.example.app_pedidos.model.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

public interface ApiService {

    String BASE_URL = "http://192.168.100.104:8000/";

    // Método para realizar login y obtener el token
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    // Método para crear un pedido (requiere token de autenticación)
    @POST("pedidos")
    Call<Pedido> createPedido(@Header("Authorization") String token, @Body Pedido pedido);

    // Método para obtener todos los pedidos (requiere token de autenticación)
    @GET("pedidos")
    Call<List<Pedido>> getPedidos(@Header("Authorization") String token);

    // Método para obtener pedidos filtrados por estado (requiere token de autenticación)
    @GET("pedidos")
    Call<List<Pedido>> getPedidosPorEstado(@Header("Authorization") String token, @Query("estado") String estado);

    @PUT("pedidos/{pedidoId}/estado")
    Call<Void> cambiarEstadoPedido(@Header("Authorization") String token, @Path("pedidoId") int pedidoId, @Body String nuevoEstado);

    // Método para obtener los detalles de un pedido específico (requiere token de autenticación)
    @GET("pedidos/{pedidoId}/detalles")
    Call<List<DetallePedido>> obtenerDetallesPedido(@Header("Authorization") String token, @Path("pedidoId") int pedidoId);

    // CRUD para productos

    // Obtener todos los productos
    @GET("productos")
    Call<List<Producto>> getProductos(@Header("Authorization") String token);

    // Crear un nuevo producto
    @POST("productos")
    Call<Producto> createProducto(@Header("Authorization") String token, @Body Producto producto);

    // Actualizar un producto existente
    @PUT("productos/{id}")
    Call<Producto> updateProducto(@Header("Authorization") String token, @Path("id") int id, @Body Producto producto);

    // Eliminar (lógicamente o físicamente) un producto
    @DELETE("productos/{id}")
    Call<Void> deleteProducto(@Header("Authorization") String token, @Path("id") int id);

    // Método para obtener una instancia de Retrofit
    static ApiService getInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiService.class);
    }

    // Método para obtener el token desde SharedPreferences
    static String getAuthToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app_pedidos", Context.MODE_PRIVATE);
        return sharedPreferences.getString("auth_token", null);  // Recuperamos el token almacenado
    }

    // Método para almacenar el token en SharedPreferences
    static void saveAuthToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app_pedidos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", token);  // Guardamos el token
        editor.apply();
    }
}
