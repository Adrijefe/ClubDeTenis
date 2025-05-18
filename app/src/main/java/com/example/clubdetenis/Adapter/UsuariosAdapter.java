package com.example.clubdetenis.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;  // Import para botón
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
    List<Usuario> usuariosList;
    List<Usuario> usuariosListFull;  // Lista completa para realizar la búsqueda

    public UsuariosAdapter(Context context, List<Usuario> usuariosList) {
        this.context = context;
        this.usuariosList = usuariosList;
        this.usuariosListFull = new ArrayList<>();
        usuariosListFull.addAll(usuariosList);
    }

    @Override
    public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    public void filtrado(final String text) {
        int textLength = text.length();
        if (textLength == 0) {
            usuariosList.clear();
            usuariosList.addAll(usuariosListFull);  // ← CORREGIDO
        } else {
            List<Usuario> coleccion = usuariosList.stream().filter(i -> i.getNombre().toLowerCase().contains(text.toLowerCase())).collect(Collectors.toList());
            usuariosList.clear();
            usuariosList.addAll(coleccion);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(UsuarioViewHolder holder, int position) {
        Usuario usuario = usuariosList.get(position);
        holder.id.setText(String.valueOf(usuario.getId()));
        holder.nombre.setText(usuario.getNombre());
        holder.email.setText(usuario.getEmail());

        // Mostrar botón eliminar siempre (puedes agregar lógica según perfil)
        holder.btnEliminar.setVisibility(View.VISIBLE);

        holder.btnEliminar.setOnClickListener(v -> {
            int positionToRemove = holder.getAdapterPosition();
            if (positionToRemove != RecyclerView.NO_POSITION) {
                eliminarUsuario(positionToRemove);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuariosList.size();
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

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {

        TextView id, nombre, email;
        Button btnEliminar;  // Botón eliminar

        public UsuarioViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.tvId);
            nombre = itemView.findViewById(R.id.tvNombre);
            email = itemView.findViewById(R.id.etEmail);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);  // Inicializar botón
        }
    }
}
