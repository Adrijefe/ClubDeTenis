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

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder> implements Filterable {

    private Context context;
    private List<Usuario> usuariosList;
    private List<Usuario> usuariosListFull;  // Lista completa para realizar la búsqueda

    public UsuariosAdapter(Context context, List<Usuario> usuariosList) {
        this.context = context;
        this.usuariosList = usuariosList;
        this.usuariosListFull = new ArrayList<>(usuariosList);  // Copia de la lista completa
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

    @Override
    public Filter getFilter() {
        return usuarioFilter;
    }

    private Filter usuarioFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Usuario> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(usuariosListFull);  // Si no hay búsqueda, mostrar todos los usuarios
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Usuario usuario : usuariosListFull) {
                    if (usuario.getNombre().toLowerCase().contains(filterPattern) ||
                            usuario.getEmail().toLowerCase().contains(filterPattern)) {
                        filteredList.add(usuario);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            usuariosList.clear();
            usuariosList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, email;

        public UsuarioViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombre);
            email = itemView.findViewById(R.id.etEmail);
        }
    }
}
