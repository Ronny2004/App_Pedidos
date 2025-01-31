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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CookActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PedidoAdapter adapter;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);

        recyclerView = findViewById(R.id.recyclerViewPedidosCocinero);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        logoutButton = findViewById(R.id.logoutButton);

        // Cargar los pedidos pendientes
        loadPedidosCocinero();

        // Configurar el botón de cerrar sesión
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void loadPedidosCocinero() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        if (token.isEmpty()) {
            //Toast.makeText(this, "Token no válido. Inicie sesión nuevamente.", Toast.LENGTH_SHORT).show();
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

    private void logout() {
        // Eliminar el token de las SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.apply();

        // Mostrar un mensaje de confirmación
        Toast.makeText(CookActivity.this, "Sesión cerrada exitosamente.", Toast.LENGTH_SHORT).show();

        // Redirigir al usuario a la pantalla de inicio de sesión
        Intent intent = new Intent(CookActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();  // Finalizar esta actividad para que no vuelva al presionar el botón "Atrás"
    }
}
