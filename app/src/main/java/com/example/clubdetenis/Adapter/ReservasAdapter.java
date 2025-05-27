package com.example.clubdetenis.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdetenis.R;
import com.example.clubdetenis.Utils.PreferenceManager;
import com.example.clubdetenis.api.ApiClient;
import com.example.clubdetenis.api.ApiService;
import com.example.clubdetenis.models.Reserva;
import com.example.clubdetenis.models.Usuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservasAdapter extends RecyclerView.Adapter<ReservasAdapter.ReservaViewHolder> {
    private Context context;
    private List<Reserva> reservaList;        // Lista que contiene las reservas que se muestran
    private List<Reserva> reservaListFull;    // Lista completa para restaurar después de filtrar

    // Constructor inicializa contexto y las listas (la copia completa para el filtrado)
    public ReservasAdapter(Context context, List<Reserva> reservaList) {
        this.context = context;
        this.reservaList = reservaList;
        this.reservaListFull = new ArrayList<>();
        reservaListFull.addAll(reservaList);
    }

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout del ítem reserva para usarlo en cada ViewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.item_reserva, parent, false);
        return new ReservaViewHolder(view);
    }

    // Método para filtrar la lista de reservas por texto en nombre de usuario o fecha
    public void filtrado(final String text) {
        if (text.isEmpty()) {
            // Si el texto está vacío, muestra toda la lista original
            reservaList.clear();
            reservaList.addAll(reservaListFull);
        } else {
            // Filtra la lista por coincidencia en usuarioNombre o que la fecha empiece con el texto
            String textoLower = text.toLowerCase();
            List<Reserva> filtrados = reservaListFull.stream()
                    .filter(r -> r.getUsuarioNombre().toLowerCase().contains(textoLower)
                            || r.getFecha().startsWith(textoLower))
                    .collect(Collectors.toList());
            reservaList.clear();
            reservaList.addAll(filtrados);
        }
        notifyDataSetChanged(); // Notifica cambios para actualizar la vista
    }

    // Actualiza la lista de reservas con una nueva lista por ejemplo, tras obtener datos nuevos
    public void setReserva(List<Reserva> nuevasReservas) {
        reservaList.clear();
        reservaList.addAll(nuevasReservas);
        reservaListFull.clear();
        reservaListFull.addAll(nuevasReservas);
        notifyDataSetChanged();
    }

    // Asocia datos de la reserva con las vistas del ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        Reserva reserva = reservaList.get(position);
        holder.tvPista.setText(reserva.getPistaNombre());
        holder.tvFecha.setText(reserva.getFecha());
        holder.tvHora.setText(String.format("%s - %s", reserva.getHoraInicio(), reserva.getHoraFin()));
        holder.tvEstado.setText(reserva.getEstado());
        holder.tvUsuarioNombre.setText(reserva.getUsuarioNombre());
        holder.tvUsuarioId.setText("ID: " + reserva.getUsuarioId());

        // Obtiene el usuario actual desde las preferencias para controlar visibilidad de botón eliminar
        PreferenceManager preferenceManager = new PreferenceManager(context);
        Usuario usuarioActual = preferenceManager.getUser();

        // Verifica si la reserva ya ha pasodo comparando fecha y hora fin con la fecha actual
        boolean yaPasado = false;
        try {
            SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String fechaHoraFinStr = reserva.getFecha() + " " + reserva.getHoraFin();
            Date fechaHoraFin = sdfFecha.parse(fechaHoraFinStr);
            Date ahora = new Date();
            if (fechaHoraFin != null && fechaHoraFin.before(ahora)) {
                yaPasado = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            yaPasado = false; // En caso de error no bloquea botón eliminar
        }

        // Muestra botón eliminar solo si es administrador o dueño de la reserva y la reserva no ha pasado
        if (("Administrador".equals(usuarioActual.getPerfil()) || usuarioActual.getId() == reserva.getUsuarioId()) && !yaPasado) {
            holder.btnEliminar.setVisibility(View.VISIBLE);
        } else {
            holder.btnEliminar.setVisibility(View.GONE);
        }

        // Listener para eliminar la reserva cuando se pulsa el botón
        holder.btnEliminar.setOnClickListener(v -> {
            int positionToRemove = holder.getAdapterPosition();
            if (positionToRemove != RecyclerView.NO_POSITION) {
                eliminarReserva(positionToRemove);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Retorna el número de reservas visibles
        return reservaList.size();
    }

    // Actualiza la lista de reservas y notifica cambios es como un metodo auxiliar mas o menos
    public void updateData(List<Reserva> newReservaList) {
        this.reservaList.clear();
        this.reservaList.addAll(newReservaList);
        notifyDataSetChanged();
    }

    // Método  para eliminar reserva de la API y actualizar la lista
    private void eliminarReserva(int position) {
        Reserva reservaAEliminar = reservaList.get(position);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.eliminarReserva(reservaAEliminar.getId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    reservaList.remove(position);      // Elimina reserva de la lista local
                    notifyItemRemoved(position);       // Notifica RecyclerView
                    Toast.makeText(context, "Reserva eliminada exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al eliminar la reserva", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Clase ViewHolder que mantiene referencias a las vistas del layout para cada reserva
    public static class ReservaViewHolder extends RecyclerView.ViewHolder {
        TextView tvPista, tvFecha, tvHora, tvEstado, tvUsuarioNombre, tvUsuarioId;
        Button btnEliminar;

        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPista = itemView.findViewById(R.id.tvPista);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvUsuarioNombre = itemView.findViewById(R.id.tvUsuarioNombre);
            tvUsuarioId = itemView.findViewById(R.id.tvUsuarioId);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
