package com.example.app_pedidos;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);

        pedidosRecyclerView = findViewById(R.id.pedidosRecyclerView);
        addPedidoButton = findViewById(R.id.addPedidoButton);

        pedidosList = new ArrayList<>();

        // Obtener el token de SharedPreferences
        String token = getToken();
        if (token == null) {
            Toast.makeText(this, "Token no encontrado. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return; // Salir de la actividad si no hay token
        }

        // Inicializar el adaptador después de obtener el token
        pedidoAdapter = new PedidoAdapter(pedidosList, token, this); // Correcto
        pedidosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pedidosRecyclerView.setAdapter(pedidoAdapter);

        // Cargar pedidos existentes al iniciar la actividad
        loadPedidos();

        addPedidoButton.setOnClickListener(this::addNewPedido);
    }

    public void addNewPedido(View view) {
        Pedido nuevoPedido = new Pedido(1, 0.0, "pendiente"); // ID usuario y estado inicial

        String token = getToken();
        if (token == null) {
            Toast.makeText(this, "Token no encontrado. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
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
}
