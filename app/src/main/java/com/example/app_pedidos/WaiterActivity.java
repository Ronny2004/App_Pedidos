package com.example.app_pedidos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_pedidos.adapter.PedidoAdapter;
import com.example.app_pedidos.api.ApiService;
import com.example.app_pedidos.model.Pedido;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaiterActivity extends AppCompatActivity {

    private RecyclerView pedidosRecyclerView;
    private PedidoAdapter pedidoAdapter;
    private List<Pedido> pedidosList;
    private Button addPedidoButton;
    private Button logoutButton; // Botón de Cerrar Sesión

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);

        pedidosRecyclerView = findViewById(R.id.pedidosRecyclerView);
        addPedidoButton = findViewById(R.id.addPedidoButton);
        logoutButton = findViewById(R.id.logoutButton); // Inicializamos el botón

        pedidosList = new ArrayList<>();

        // Inicializar el adaptador sin necesidad de token en onCreate()
        pedidosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pedidosRecyclerView.setAdapter(pedidoAdapter);

        // Cargar pedidos existentes al iniciar la actividad
        loadPedidos();

        addPedidoButton.setOnClickListener(this::addNewPedido);

        // Configurar el botón de cerrar sesión
        logoutButton.setOnClickListener(this::logout);
    }

    public void addNewPedido(View view) {
        Pedido nuevoPedido = new Pedido(1, 0.0, "pendiente");

        String token = getToken();
        if (token == null) {
            //Toast.makeText(this, "Token no encontrado. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiService.getInstance();
        Call<Pedido> call = apiService.createPedido(token, nuevoPedido);

        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pedidosList.add(0, response.body()); // Agregar al inicio
                    pedidoAdapter.notifyItemInserted(0);
                    Toast.makeText(WaiterActivity.this, "Pedido agregado exitosamente.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WaiterActivity.this, "Error al agregar el pedido: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Toast.makeText(WaiterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPedidos() {
        String token = getToken();
        if (token == null) {
            Toast.makeText(this, "Token no encontrado. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiService.getInstance();
        Call<List<Pedido>> call = apiService.getPedidos(token);

        call.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pedidosList.clear();
                    pedidosList.addAll(response.body());
                    pedidoAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(WaiterActivity.this, "Error al cargar los pedidos.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(WaiterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("token", null); // Asegúrate de que el token esté guardado aquí
    }

    // Método para cerrar sesión
    private void logout(View view) {
        // Limpiar SharedPreferences para borrar el token
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.apply();

        // Redirigir al login sin lógica adicional
        Intent intent = new Intent(WaiterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Finaliza la actividad actual para evitar que el usuario regrese a ella
    }
}
