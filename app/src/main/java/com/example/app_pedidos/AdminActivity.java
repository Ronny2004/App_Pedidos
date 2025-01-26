package com.example.app_pedidos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

public class AdminActivity extends AppCompatActivity {

    private Button btnManageProducts, btnManageUsers, btnViewReports;
    private RecyclerView recyclerPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);  // Asegúrate de que el nombre es correcto

        // Inicialización de vistas
        btnManageProducts = findViewById(R.id.manage_products);
        btnManageUsers = findViewById(R.id.manage_users);
        btnViewReports = findViewById(R.id.view_reports);
        recyclerPedidos = findViewById(R.id.recycler_pedidos);

        // Configuración de RecyclerView
        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));

        // Eventos de botones
        btnManageProducts.setOnClickListener(v -> {
            // Redirigir a la actividad ProductoFormActivity
            Intent intent = new Intent(AdminActivity.this, ProductoFormActivity.class);
            startActivity(intent);
        });

        btnManageUsers.setOnClickListener(v -> {
            Toast.makeText(AdminActivity.this, "Gestionar Usuarios", Toast.LENGTH_SHORT).show();
        });

        btnViewReports.setOnClickListener(v -> {
            Toast.makeText(AdminActivity.this, "Ver Reportes", Toast.LENGTH_SHORT).show();
        });
    }
}
