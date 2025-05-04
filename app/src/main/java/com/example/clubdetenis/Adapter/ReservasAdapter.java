package com.example.clubdetenis.Adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.clubdetenis.R;
import com.example.clubdetenis.models.Reserva;

import java.util.List;

public class ReservasAdapter extends RecyclerView.Adapter<ReservasAdapter.ReservaViewHolder> {
    private Context context;
    private List<Reserva> reservaList;

    public ReservasAdapter(Context context, List<Reserva> reservaList) {
        this.context = context;
        this.reservaList = reservaList;
    }

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reserva, parent, false);
        return new ReservaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        Reserva reserva = reservaList.get(position);
        holder.tvPista.setText(reserva.getPistaNombre());
        holder.tvFecha.setText(reserva.getFecha());
        holder.tvHora.setText(String.format("%s - %s", reserva.getHoraInicio(), reserva.getHoraFin()));
        holder.tvEstado.setText(reserva.getEstado());
    }

    @Override
    public int getItemCount() {
        return reservaList.size();
    }

    public static class ReservaViewHolder extends RecyclerView.ViewHolder {
        TextView tvPista, tvFecha, tvHora, tvEstado;

        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPista = itemView.findViewById(R.id.tvPista);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}