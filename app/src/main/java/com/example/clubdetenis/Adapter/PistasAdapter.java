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

// Adaptador para mostrar una lista de pistas en un RecyclerView
public class PistasAdapter extends RecyclerView.Adapter<PistasAdapter.PistaViewHolder> {

    private Context context;             // Contexto de la actividad o fragmento
    private List<Pista> pistaList;       // Lista de objetos Pista a mostrar

    // Constructor del adaptador
    public PistasAdapter(Context context, List<Pista> pistaList) {
        this.context = context;
        this.pistaList = pistaList;
    }

    // Crea nuevas vistas
    @NonNull
    @Override
    public PistaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout de cada ítem (item_pista.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.item_pista, parent, false);
        return new PistaViewHolder(view);
    }

    // Asocia los datos con cada vista (invocado por el LayoutManager)
    @Override
    public void onBindViewHolder(@NonNull PistaViewHolder holder, int position) {
        // Obtiene la pista en la posición actual
        Pista pista = pistaList.get(position);

        // Establece los valores en las vistas del ViewHolder
        holder.tvNombre.setText(pista.getNombre());
        holder.tvTipo.setText(pista.getTipo());
        holder.tvPrecio.setText(pista.getDescripcion());
    }

    // Devuelve la cantidad de ítems en la lista
    @Override
    public int getItemCount() {
        return pistaList.size();
    }

    // Clase interna que representa cada ítem (ViewHolder)
    public static class PistaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvTipo, tvPrecio;  // Vistas del layout item_pista

        // Constructor del ViewHolder: obtiene referencias a las vistas del layout
        public PistaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
        }
    }
}
