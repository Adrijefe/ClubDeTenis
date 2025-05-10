package com.example.clubdetenis.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdetenis.R;
import com.example.clubdetenis.models.Usuario;

import java.util.List;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder> {

    private Context context;
    private List<Usuario> usuariosList;

    public UsuariosAdapter(Context context, List<Usuario> usuariosList) {
        this.context = context;
        this.usuariosList = usuariosList;
    }

    @Override
    public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsuarioViewHolder holder, int position) {
        Usuario usuario = usuariosList.get(position);
        holder.nombre.setText(usuario.getNombre());
        holder.email.setText(usuario.getEmail());
    }

    @Override
    public int getItemCount() {
        return usuariosList.size();
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, email;

        public UsuarioViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombre);
            email = itemView.findViewById(R.id.etEmail);
        }
    }
}
