package com.example.clubdetenis.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.clubdetenis.R;
import com.example.clubdetenis.models.Pista;

import java.util.List;

public class PistasAdapter extends RecyclerView.Adapter<PistasAdapter.PistaViewHolder> {
    private Context context;
    private List<Pista> pistaList;

    public PistasAdapter(Context context, List<Pista> pistaList) {
        this.context = context;
        this.pistaList = pistaList;
    }

    @NonNull
    @Override
    public PistaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pista, parent, false);
        return new PistaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PistaViewHolder holder, int position) {
        Pista pista = pistaList.get(position);
        holder.tvNombre.setText(pista.getNombre());
        holder.tvTipo.setText(pista.getTipo());
        holder.tvPrecio.setText(String.format("€%.2f/hora", pista.getPrecioHora()));
    }

    @Override
    public int getItemCount() {
        return pistaList.size();
    }

    public static class PistaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvTipo, tvPrecio;

        public PistaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
        }
    }
}
