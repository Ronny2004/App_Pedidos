package com.example.app_pedidos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.app_pedidos.WaiterActivity;
import com.example.app_pedidos.CashierActivity;

import com.example.app_pedidos.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_pedidos.api.ApiService;
import com.example.app_pedidos.model.Pedido;
import com.example.app_pedidos.model.DetallePedido; // Suponiendo que tienes una clase para el detalle del pedido.

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private final List<Pedido> pedidos;
    private final String token;
    private final Context context;

    // Constructor
    public PedidoAdapter(List<Pedido> pedidos, String token, Context context) {
        this.pedidos = pedidos;
        this.token = token;
        this.context = context;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        holder.txtDetallePedido.setText("Pedido ID: " + pedido.getId());

        // Obtener detalles del pedido desde la API
        obtenerDetallesPedido(pedido.getId(), holder);

        holder.btnPreparando.setOnClickListener(v -> cambiarEstadoPedido(pedido.getId(), "preparando"));
        holder.btnListo.setOnClickListener(v -> cambiarEstadoPedido(pedido.getId(), "listo"));
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    // Método para obtener los detalles del pedido desde la API
    private void obtenerDetallesPedido(int pedidoId, PedidoViewHolder holder) {
        ApiService apiService = ApiService.getInstance();
        Call<List<DetallePedido>> call = apiService.obtenerDetallesPedido("Bearer " + token, pedidoId);

        call.enqueue(new Callback<List<DetallePedido>>() {
            @Override
            public void onResponse(Call<List<DetallePedido>> call, Response<List<DetallePedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DetallePedido> detalles = response.body();
                    StringBuilder detallesPedidoText = new StringBuilder("Detalles:\n");

                    // Mostrar los detalles de los productos
                    for (DetallePedido detalle : detalles) {
                        detallesPedidoText.append("Producto ID: ").append(detalle.getIdProducto())
                                .append(" - Cantidad: ").append(detalle.getCantidad())
                                .append(" - Precio: ").append(detalle.getPrecioUnitario()).append("\n");
                    }

                    holder.txtDetallePedido.append("\n" + detallesPedidoText.toString());
                } else {
                    Toast.makeText(context, "Error al obtener los detalles del pedido.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DetallePedido>> call, Throwable t) {
                Toast.makeText(context, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para cambiar el estado del pedido
    private void cambiarEstadoPedido(int pedidoId, String nuevoEstado) {
        ApiService apiService = ApiService.getInstance();
        Call<Void> call = apiService.cambiarEstadoPedido("Bearer " + token, pedidoId, nuevoEstado);

        // Enviando la petición
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Estado cambiado a " + nuevoEstado, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al cambiar estado del pedido. Código: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ViewHolder para el adaptador
    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView txtDetallePedido;
        Button btnPreparando, btnListo;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDetallePedido = itemView.findViewById(R.id.txtDetallePedido);
            btnPreparando = itemView.findViewById(R.id.btnPreparando);
            btnListo = itemView.findViewById(R.id.btnListo);
        }
    }
}
