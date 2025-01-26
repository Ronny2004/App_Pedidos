package com.example.app_pedidos;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_pedidos.api.ApiService;
import com.example.app_pedidos.model.Producto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoFormActivity extends AppCompatActivity {

    private EditText nombreInput, descripcionInput, precioInput;
    private Spinner tipoSpinner, estadoSpinner;
    private Button saveButton;

    private ApiService apiService;
    private Producto producto; // Para editar un producto existente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_form);

        nombreInput = findViewById(R.id.nombreInput);
        descripcionInput = findViewById(R.id.descripcionInput);
        precioInput = findViewById(R.id.precioInput);
        tipoSpinner = findViewById(R.id.tipoSpinner);
        estadoSpinner = findViewById(R.id.estadoSpinner);
        saveButton = findViewById(R.id.saveButton);

        apiService = ApiService.getInstance();

        // Cargar datos si es edición
        if (getIntent().hasExtra("producto")) {
            producto = (Producto) getIntent().getSerializableExtra("producto");
            loadProductoData();
        }

        // Poblamos los Spinners con los valores posibles
        populateSpinners();

        // Guardar producto
        saveButton.setOnClickListener(v -> saveProducto());
    }

    private void loadProductoData() {
        nombreInput.setText(producto.getNombre());
        descripcionInput.setText(producto.getDescripcion());
        precioInput.setText(String.valueOf(producto.getPrecio()));

        // Seleccionar valores en los Spinners según el producto
        int tipoPosition = getSpinnerPosition(tipoSpinner, producto.getTipo());
        tipoSpinner.setSelection(tipoPosition);

        int estadoPosition = getSpinnerPosition(estadoSpinner, producto.getEstado());
        estadoSpinner.setSelection(estadoPosition);
    }

    // Método para encontrar la posición de un item en el Spinner
    private int getSpinnerPosition(Spinner spinner, String item) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(item)) {
                return i;
            }
        }
        return 0; // Retorna la primera posición si no se encuentra
    }

    // Poblamos los Spinners con las opciones posibles
    private void populateSpinners() {
        // Opciones para el Spinner de tipo
        String[] tipos = {"plato", "bebida", "postre", "adicional"};
        ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoSpinner.setAdapter(tipoAdapter);

        // Opciones para el Spinner de estado
        String[] estados = {"disponible", "agotado", "pronto"};
        ArrayAdapter<String> estadoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estados);
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estadoSpinner.setAdapter(estadoAdapter);
    }

    private void saveProducto() {
        String token = ApiService.getAuthToken(ProductoFormActivity.this);  // Recuperamos el token

        if (token == null) {
            Toast.makeText(ProductoFormActivity.this, "No has iniciado sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombre = nombreInput.getText().toString().trim();
        String descripcion = descripcionInput.getText().toString().trim();
        String precioStr = precioInput.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(ProductoFormActivity.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            Toast.makeText(ProductoFormActivity.this, "Precio inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        String tipo = tipoSpinner.getSelectedItem().toString();
        String estado = estadoSpinner.getSelectedItem().toString();

        Producto newProducto = new Producto(0, nombre, descripcion, precio, tipo, estado);

        Call<Producto> call;
        if (producto != null) {
            // Editar producto existente
            call = apiService.updateProducto("Bearer " + token, producto.getId(), newProducto);
        } else {
            // Crear nuevo producto
            call = apiService.createProducto("Bearer " + token, newProducto);
        }

        call.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductoFormActivity.this, "Producto guardado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Mostrar detalles de la respuesta del servidor para depuración
                    String errorMessage = "Error al guardar producto. Código: " + response.code() +
                            ", Mensaje: " + response.message() +
                            ", Cuerpo: " + response.errorBody();
                    Toast.makeText(ProductoFormActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                Toast.makeText(ProductoFormActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
