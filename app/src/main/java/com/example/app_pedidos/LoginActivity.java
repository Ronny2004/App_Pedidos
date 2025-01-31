package com.example.app_pedidos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_pedidos.api.ApiService;
import com.example.app_pedidos.model.LoginRequest;
import com.example.app_pedidos.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ImageView logoImage;
    private static final String PREF_NAME = "app_pedidos";
    private static final String TOKEN_KEY = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Aplicar animaciones
        View layout = findViewById(R.id.loginLayout);
        logoImage = findViewById(R.id.logoImage);

        if (layout != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            layout.startAnimation(fadeIn);
        }

        if (logoImage != null) {
            Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            logoImage.startAnimation(slideUp);
        }

        // Inicializar vistas
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        // Configurar evento para botón de login
        loginButton.setOnClickListener(v -> {
            String email = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (validateInputs(email, password)) {
                performLogin(email, password);
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            usernameEditText.setError("Ingrese su correo");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernameEditText.setError("Correo inválido");
            return false;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Ingrese su contraseña");
            return false;
        }
        return true;
    }

    private void performLogin(String email, String password) {
        ApiService apiService = ApiService.getInstance();
        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<LoginResponse> call = apiService.login(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handleSuccessfulLogin(response.body());
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LoginError", "Error de conexión: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "No se pudo conectar al servidor.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleSuccessfulLogin(LoginResponse loginResponse) {
        if ("inactivo".equalsIgnoreCase(loginResponse.getEstado())) {
            Toast.makeText(LoginActivity.this, "El usuario está inactivo. Contacte al administrador.", Toast.LENGTH_LONG).show();
            return;
        }
        saveToken(loginResponse.getToken());
        Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("USER_ROLE", loginResponse.getRol());
        startActivity(intent);
        finish();
    }

    private void handleErrorResponse(Response<LoginResponse> response) {
        try {
            String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Error desconocido";
            Log.e("LoginError", "Error: " + errorMessage);
            Toast.makeText(LoginActivity.this, "Credenciales incorrectas.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("LoginError", "Exception: " + e.getMessage());
            Toast.makeText(LoginActivity.this, "Error desconocido.", Toast.LENGTH_LONG).show();
        }
    }

    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }
}
