package com.example.app_pedidos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener el rol desde el Intent
        Intent intent = getIntent();
        String userRole = intent.getStringExtra("USER_ROLE");

        // Validar que el rol no sea nulo
        if (userRole == null) {
            Toast.makeText(this, "Error: Rol no encontrado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Mostrar el mensaje de bienvenida
        String welcomeMessage = "BIENVENIDO/A " + userRole.toUpperCase();
        Toast.makeText(this, welcomeMessage, Toast.LENGTH_LONG).show();

        // Redirigir según el rol
        redirectToRoleActivity(userRole);
    }

    private void redirectToRoleActivity(String userRole) {
        Class<?> targetActivity;

        switch (userRole.toLowerCase()) {
            case "administrador":
                targetActivity = AdminActivity.class;
                break;
            case "mesero":
                targetActivity = WaiterActivity.class;
                break;
            case "cocinero":
                targetActivity = CookActivity.class;
                break;
            case "cajero":
                targetActivity = CashierActivity.class;
                break;
            default:
                Toast.makeText(this, "Rol desconocido: " + userRole, Toast.LENGTH_SHORT).show();
                return;
        }

        // Inicia la actividad correspondiente
        Intent intent = new Intent(MainActivity.this, targetActivity);
        startActivity(intent);
        finish(); // Finaliza el MainActivity para evitar regresar
    }
}
