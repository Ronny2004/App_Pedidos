package com.example.app_pedidos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.Serializable; // Añadir la importación de Serializable
import java.util.List;
import java.util.stream.Collectors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_pedidos.api.ApiService;
import com.example.app_pedidos.model.Producto;
import com.example.app_pedidos.adapter.ProductosAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductosActivity extends AppCompatActivity implements ProductosAdapter.OnProductoActionListener {

    private RecyclerView recyclerView;
    private ProductosAdapter adapter;
    private EditText searchInput;
    private Button searchButton, addButton;
    private ProgressBar progressBar;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        recyclerView = findViewById(R.id.recyclerView);
        searchInput = findViewById(R.id.searchInput);
        searchButton = findViewById(R.id.searchButton);
        addButton = findViewById(R.id.addButton);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiService = ApiService.getInstance();

        // Cargar la lista de productos
        loadProductos();

        // Botón para buscar productos
        searchButton.setOnClickListener(v -> searchProductos(searchInput.getText().toString()));

        // Botón para agregar un producto
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProductosActivity.this, ProductoFormActivity.class);
            startActivity(intent);
        });
    }

    // Método para cargar todos los productos
    private void loadProductos() {
        progressBar.setVisibility(View.VISIBLE);
        String token = "Bearer YOUR_TOKEN"; // Aquí deberías recuperar el token de forma segura
        apiService.getProductos(token).enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> productos = response.body();
                    adapter = new ProductosAdapter(ProductosActivity.this, productos, ProductosActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ProductosActivity.this, "Error al cargar productos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProductosActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para buscar productos por nombre
    private void searchProductos(String nombre) {
        if (nombre.isEmpty()) {
            loadProductos();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        apiService.getProductos("Bearer YOUR_TOKEN").enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> productos = response.body();
                    // Reemplazar .toList() por collect(Collectors.toList())
                    List<Producto> filtrados = productos.stream()
                            .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                            .collect(Collectors.toList()); // Cambié aquí

                    adapter = new ProductosAdapter(ProductosActivity.this, filtrados, ProductosActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ProductosActivity.this, "Error al buscar productos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProductosActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para filtrar los productos por nombre
    private List<Producto> filterProductosByNombre(List<Producto> productos, String nombre) {
        return productos.stream()
                .filter(producto -> producto.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList()); // Cambié aquí a Collectors.toList()
    }

    // Implementación del método de la interfaz OnProductoActionListener
    @Override
    public void onEditProducto(Producto producto) {
        Intent intent = new Intent(ProductosActivity.this, ProductoFormActivity.class);
        intent.putExtra("producto", producto); // Ahora Producto implementa Serializable
        startActivity(intent);
    }


    @Override
    public void onDeleteProducto(Producto producto) {
        // Aquí podrías agregar el código para eliminar el producto, por ejemplo, llamando a una API para eliminarlo.
        Toast.makeText(ProductosActivity.this, "Producto eliminado: " + producto.getNombre(), Toast.LENGTH_SHORT).show();
        // Después de eliminarlo, recargar los productos
        loadProductos();
    }
}
