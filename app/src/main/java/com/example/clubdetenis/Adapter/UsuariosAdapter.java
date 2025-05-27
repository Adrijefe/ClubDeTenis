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
// Constructor
    public UsuariosAdapter(Context context, List<Usuario> usuariosList, boolean mostrarBotonEliminar, boolean mostrarTelefono) {

        this.context = context;
        this.usuariosList = usuariosList;
        this.usuariosListFull = new ArrayList<>();
        this.usuariosListFull.addAll(usuariosList);
        this.mostrarBotonEliminar = mostrarBotonEliminar;
        this.mostrarTelefono = mostrarTelefono;
    }
    // Infla el layout de cada ítem de usuario y crea el ViewHolder
    @Override
    public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }
    // Asocia los datos del usuario a las vistas y configura visibilidad y eventos del botón eliminar y teléfono
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
    // Elimina un usuario llamando a la API y actualiza la lista en caso de éxito o muestra error
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
    // Actualiza la lista completa de usuarios y refresca la vista del RecyclerView
    public void setUsuarios(List<Usuario> nuevosUsuarios) {
        usuariosList.clear();
        usuariosList.addAll(nuevosUsuarios);
        usuariosListFull.clear();
        usuariosListFull.addAll(nuevosUsuarios);
        notifyDataSetChanged();
    }
    // Filtra la lista de usuarios por nombre, teléfono o email y actualiza la vista
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
    // Devuelve el número total de usuarios mostrados en la lista
    @Override
    public int getItemCount() {
        return usuariosList.size();
    }
// mantiene las referencias a las vistas de cada ítem (id, nombre, email, teléfono y botón eliminar)
    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {

        TextView id, nombre, email, telefono;
        Button btnEliminar;

        public UsuarioViewHolder(View itemView) {
            // Constructor que vincula vistas (TextViews, Button) con variables para usarlas en onBindViewHolder
            super(itemView);
            id = itemView.findViewById(R.id.tvId);
            nombre = itemView.findViewById(R.id.tvNombre);
            email = itemView.findViewById(R.id.etEmail);
            telefono = itemView.findViewById(R.id.tvTelefono);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
