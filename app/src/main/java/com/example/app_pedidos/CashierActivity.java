package com.example.app_pedidos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_pedidos.adapter.PedidoAdapter;
import com.example.app_pedidos.api.ApiService;
import com.example.app_pedidos.model.Pedido;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CashierActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PedidoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);

        recyclerView = findViewById(R.id.recyclerViewPedidosCajero);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadPedidosFinalizados();
    }

    private void loadPedidosFinalizados() {
        // Obtener el token de SharedPreferences
        String token = getAuthToken();

        if (token.isEmpty()) {
            Toast.makeText(CashierActivity.this, "Token de autenticación no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiService.getInstance();
        Call<List<Pedido>> call = apiService.getPedidosPorEstado("Bearer " + token, "finalizado");

        call.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new PedidoAdapter(response.body(), token, CashierActivity.this); // Correcto
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(CashierActivity.this, "No se pudieron cargar los pedidos finalizados.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(CashierActivity.this, "Error al cargar los pedidos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para obtener el token desde SharedPreferences
    private String getAuthToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_pedidos", MODE_PRIVATE);
        return sharedPreferences.getString("auth_token", ""); // Verifica que el token esté guardado aquí
    }
}
