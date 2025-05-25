package com.example.clubdetenis.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdetenis.R;
import com.example.clubdetenis.api.ApiClient;
import com.example.clubdetenis.api.ApiService;
import com.example.clubdetenis.models.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder> {

    private Context context;
    private List<Usuario> usuariosList;
    private List<Usuario> usuariosListFull;
    private boolean mostrarBotonEliminar;
    private boolean mostrarTelefono;

    public UsuariosAdapter(Context context, List<Usuario> usuariosList, boolean mostrarBotonEliminar, boolean mostrarTelefono) {
        this.context = context;
        this.usuariosList = usuariosList;
        this.usuariosListFull = new ArrayList<>();
        this.usuariosListFull.addAll(usuariosList);
        this.mostrarBotonEliminar = mostrarBotonEliminar;
        this.mostrarTelefono = mostrarTelefono;
    }

    @Override
    public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsuarioViewHolder holder, int position) {
        Usuario usuario = usuariosList.get(position);
        holder.id.setText(String.valueOf(usuario.getId()));
        holder.nombre.setText(usuario.getNombre());
        holder.email.setText(usuario.getEmail());

        if (mostrarTelefono) {
            holder.telefono.setText(usuario.getTelefono());
            holder.telefono.setVisibility(View.VISIBLE);
        } else {
            holder.telefono.setVisibility(View.GONE);
        }

        if (mostrarBotonEliminar) {
            holder.btnEliminar.setVisibility(View.VISIBLE);
            holder.btnEliminar.setOnClickListener(v -> {
                int positionToRemove = holder.getAdapterPosition();
                if (positionToRemove != RecyclerView.NO_POSITION) {
                    eliminarUsuario(positionToRemove);
                }
            });
        } else {
            holder.btnEliminar.setVisibility(View.GONE);
            holder.btnEliminar.setOnClickListener(null);
        }
    }

    private void eliminarUsuario(int position) {
        Usuario usuarioAEliminar = usuariosList.get(position);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.eliminarUsuario(usuarioAEliminar.getNombre(), usuarioAEliminar.getEmail());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    usuariosList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Usuario eliminado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al eliminar el usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setUsuarios(List<Usuario> nuevosUsuarios) {
        usuariosList.clear();
        usuariosList.addAll(nuevosUsuarios);
        usuariosListFull.clear();
        usuariosListFull.addAll(nuevosUsuarios);
        notifyDataSetChanged();
    }

    public void filtrado(final String text) {
        if (text.isEmpty()) {
            usuariosList.clear();
            usuariosList.addAll(usuariosListFull);
        } else {
            String textoLower = text.toLowerCase();
            List<Usuario> filtrados = usuariosListFull.stream()
                    .filter(u -> u.getNombre().toLowerCase().contains(textoLower)
                            || (u.getTelefono() != null && u.getTelefono().toLowerCase().contains(textoLower))
                            || (u.getEmail() != null && u.getEmail().toLowerCase().contains(textoLower)))
                    .collect(Collectors.toList());
            usuariosList.clear();
            usuariosList.addAll(filtrados);
        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return usuariosList.size();
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {

        TextView id, nombre, email, telefono;
        Button btnEliminar;

        public UsuarioViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.tvId);
            nombre = itemView.findViewById(R.id.tvNombre);
            email = itemView.findViewById(R.id.etEmail);
            telefono = itemView.findViewById(R.id.tvTelefono);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
