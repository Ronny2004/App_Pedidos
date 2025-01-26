// CookActivity.java
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

public class CookActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PedidoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);

        recyclerView = findViewById(R.id.recyclerViewPedidosCocinero);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadPedidosCocinero();
    }

    private void loadPedidosCocinero() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        if (token.isEmpty()) {
            Toast.makeText(this, "Token no válido. Inicie sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiService.getInstance();
        Call<List<Pedido>> call = apiService.getPedidosPorEstado("Bearer " + token, "pendiente");

        call.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new PedidoAdapter(response.body(), token, CookActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(CookActivity.this, "Error al cargar pedidos pendientes.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(CookActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
