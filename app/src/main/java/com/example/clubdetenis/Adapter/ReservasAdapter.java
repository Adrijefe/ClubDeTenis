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
    private List<Reserva> reservaList;
    private List<Reserva> reservaListFull;

    public ReservasAdapter(Context context, List<Reserva> reservaList) {
        this.context = context;
        this.reservaList = reservaList;
        this.reservaListFull = new ArrayList<>();
        reservaListFull.addAll(reservaList);
    }

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reserva, parent, false);
        return new ReservaViewHolder(view);
    }

    public void filtrado(final String text) {
        int textLength = text.length();
        if (textLength == 0) {
            reservaList.clear();
            reservaList.addAll(reservaListFull);  // ← CORREGIDO
        } else {
            List<Reserva> coleccion = reservaList.stream().filter(i -> i.getUsuarioNombre().toLowerCase().contains(text.toLowerCase())).collect(Collectors.toList());
            reservaList.clear();
            reservaList.addAll(coleccion);
        }
        notifyDataSetChanged();
    }
    public void setReserva(List<Reserva> nuevasReservas) {
        reservaList.clear();
        reservaList.addAll(nuevasReservas);
        reservaListFull.clear();
        reservaListFull.addAll(nuevasReservas);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        Reserva reserva = reservaList.get(position);
        holder.tvPista.setText(reserva.getPistaNombre());
        holder.tvFecha.setText(reserva.getFecha());
        holder.tvHora.setText(String.format("%s - %s", reserva.getHoraInicio(), reserva.getHoraFin()));
        holder.tvEstado.setText(reserva.getEstado());
        holder.tvUsuarioNombre.setText(reserva.getUsuarioNombre());
        holder.tvUsuarioId.setText("ID: " + reserva.getUsuarioId());

        PreferenceManager preferenceManager = new PreferenceManager(context);
        Usuario usuarioActual = preferenceManager.getUser();

        // Parsear fecha y hora de la reserva
        boolean yaPasado = false;
        try {
            SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            // Concatenamos fecha y hora fin para comparar con la hora actual
            String fechaHoraFinStr = reserva.getFecha() + " " + reserva.getHoraFin();
            Date fechaHoraFin = sdfFecha.parse(fechaHoraFinStr);

            Date ahora = new Date();

            if (fechaHoraFin != null && fechaHoraFin.before(ahora)) {
                yaPasado = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Por seguridad, si falla el parseo, dejamos visible el botón (o cambiar según prefieras)
            yaPasado = false;
        }

        if ( ("Administrador".equals(usuarioActual.getPerfil()) || usuarioActual.getId() == reserva.getUsuarioId()) && !yaPasado ) {
            holder.btnEliminar.setVisibility(View.VISIBLE);
        } else {
            holder.btnEliminar.setVisibility(View.GONE);
        }

        holder.btnEliminar.setOnClickListener(v -> {
            int positionToRemove = holder.getAdapterPosition();
            if (positionToRemove != RecyclerView.NO_POSITION) {
                eliminarReserva(positionToRemove);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservaList.size();
    }

    public void updateData(List<Reserva> newReservaList) {
        this.reservaList.clear();
        this.reservaList.addAll(newReservaList);
        notifyDataSetChanged();
    }

    private void eliminarReserva(int position) {
        Reserva reservaAEliminar = reservaList.get(position);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.eliminarReserva(reservaAEliminar.getId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    reservaList.remove(position);
                    notifyItemRemoved(position);
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
            tvUsuarioId = itemView.findViewById(R.id.tvUsuarioId);  // Nuevo TextView para el ID
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
