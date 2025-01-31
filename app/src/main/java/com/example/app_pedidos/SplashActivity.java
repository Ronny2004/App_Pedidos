package com.example.app_pedidos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView txtBienvenida = findViewById(R.id.txtBienvenida);
        TextView txtHecho = findViewById(R.id.txtHecho);
        TextView txtAppRestaurante = findViewById(R.id.txtAppRestaurante);

        // Mostrar la pantalla por 2.2 segundos antes de animar
        new Handler().postDelayed(() -> {
            // Cargar la animación de desplazamiento
            Animation anim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.desplazamiento_arriba);
            txtBienvenida.startAnimation(anim);

            // Cargar la animación de desplazamiento
            Animation anim1 = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.desplazamiento_arriba);
            txtHecho.startAnimation(anim1);

            // Esperar a que termine la animación antes de cambiar de pantalla
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }, 800);
        }, 2200);
    }
}
