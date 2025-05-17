package com.example.clubdetenis.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdetenis.R;
import com.example.clubdetenis.models.Usuario;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder> {

    private Context context;
     List<Usuario> usuariosList;
     List<Usuario> usuariosListFull;  // Lista completa para realizar la búsqueda


    public UsuariosAdapter(Context context, List<Usuario> usuariosList) {
        this.context = context;
        this.usuariosList = usuariosList;
        this.usuariosListFull = new ArrayList<>();// Copia de la lista completa
        usuariosListFull.addAll(usuariosList);

    }

    @Override
    public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    public void filtrado( final String text) {
        int textLength = text.length();
        if (textLength == 0) {
            usuariosList.clear();
            usuariosList.addAll(usuariosListFull);

        } else{
            List<Usuario> caleccion = usuariosList.stream().filter(i-> i.getNombre()
                    .toLowerCase().contains(text.toLowerCase())).collect(Collectors.toList());
            usuariosList.clear();
            usuariosList.addAll(caleccion);
    }
        notifyDataSetChanged();
}

    @Override
    public void onBindViewHolder(UsuarioViewHolder holder, int position) {
        Usuario usuario = usuariosList.get(position);
        holder.id.setText(String.valueOf(usuario.getId()));  // Añadido id
        holder.nombre.setText(usuario.getNombre());
        holder.email.setText(usuario.getEmail());
    }

    @Override
    public int getItemCount() {
        return usuariosList.size();
    }


    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {

        TextView id, nombre, email;

        public UsuarioViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.tvId);           // Inicializamos el TextView del id
            nombre = itemView.findViewById(R.id.tvNombre);
            email = itemView.findViewById(R.id.etEmail);
        }
    }
}
