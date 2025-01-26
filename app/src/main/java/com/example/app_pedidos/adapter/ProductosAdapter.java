package com.example.app_pedidos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_pedidos.R;
import com.example.app_pedidos.model.Producto;

import java.util.List;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder> {

    private final Context context;
    private final List<Producto> productos;
    private final OnProductoActionListener listener;

    public ProductosAdapter(Context context, List<Producto> productos, OnProductoActionListener listener) {
        this.context = context;
        this.productos = productos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);

        // Set data to the views
        holder.nombreTextView.setText(producto.getNombre());
        holder.descripcionTextView.setText(producto.getDescripcion());
        holder.precioTextView.setText(String.format("$%.2f", producto.getPrecio()));
        holder.tipoTextView.setText(producto.getTipo());
        holder.estadoTextView.setText(producto.getEstado());

        // Handle Edit button click
        holder.editarButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditProducto(producto);
            }
        });

        // Handle Delete button click
        holder.eliminarButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteProducto(producto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView, descripcionTextView, precioTextView, tipoTextView, estadoTextView;
        Button editarButton, eliminarButton;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombre_producto);
            descripcionTextView = itemView.findViewById(R.id.descripcion_producto);
            precioTextView = itemView.findViewById(R.id.precio_producto);
            tipoTextView = itemView.findViewById(R.id.tipo_producto);
            estadoTextView = itemView.findViewById(R.id.estado_producto);
            editarButton = itemView.findViewById(R.id.btn_editar);
            eliminarButton = itemView.findViewById(R.id.btn_eliminar);
        }
    }

    // Interface for handling actions on products
    public interface OnProductoActionListener {
        void onEditProducto(Producto producto);
        void onDeleteProducto(Producto producto);
    }
}
