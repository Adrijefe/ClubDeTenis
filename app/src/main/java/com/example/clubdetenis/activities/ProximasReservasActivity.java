//package com.example.clubdetenis.activities;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.clubdetenis.Adapter.ReservasAdapter;
//import com.example.clubdetenis.R;
//import com.example.clubdetenis.Utils.PreferenceManager;
//import com.example.clubdetenis.api.ApiClient;
//import com.example.clubdetenis.api.ApiService;
//import com.example.clubdetenis.models.Reserva;
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.reflect.TypeToken;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class ProximasReservasActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private ReservasAdapter adapter;
//    private List<Reserva> reservaList = new ArrayList<>();
//    private PreferenceManager preferenceManager;
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);  // Cambia el layout si es necesario
//
//        // Inicializamos el PreferenceManager y obtenemos el ID del usuario
//        preferenceManager = new PreferenceManager(this);
//        int usuarioId = preferenceManager.getUser().getId();
//
//        // Configurar el RecyclerView para mostrar las reservas
//        recyclerView = findViewById(R.id.recyclerViewReservas);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new ReservasAdapter(this, reservaList);
//        recyclerView.setAdapter(adapter);
//
//        // Cargar las reservas al inicio
//        loadReservas(usuarioId);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // Obtener el ID del usuario desde las preferencias
//        int usuarioId = preferenceManager.getUser().getId();
//
//        // Recargar las reservas cada vez que la actividad se vuelve visible
//        loadReservas(usuarioId);
//    }
//
//    private void loadReservas(int usuarioId) {
//        ApiService apiService = ApiClient.getClient().create(ApiService.class);
//
//        // Llamada a la API para obtener las reservas (solo hoy)
//        Call<JsonObject> call = apiService.getReservasHoy(true);  // Ya no es List<Reserva>, sino JsonObject.
//
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    try {
//                        JsonObject json = response.body();
//                        JsonArray reservasJson = json.getAsJsonArray("reservas");
//
//                        // Usamos Gson para convertir el JsonArray en una lista de objetos Reserva
//                        Gson gson = new Gson();
//                        Type listType = new TypeToken<List<Reserva>>() {}.getType();
//                        List<Reserva> reservas = gson.fromJson(reservasJson, listType);
//
//                        // Limpiar la lista de reservas y agregar las nuevas
//                        reservaList.clear();
//                        reservaList.addAll(reservas);
//                        adapter.notifyDataSetChanged();  // Notificar al adaptador que los datos han cambiado
//
//                    } catch (Exception e) {
//                        Log.e("ProximasReservasActivity", "Error al parsear JSON", e);
//                        Toast.makeText(ProximasReservasActivity.this, "Error al procesar datos", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Log.e("ProximasReservasActivity", "Respuesta del servidor no exitosa, código: " + response.code());
//                    Toast.makeText(ProximasReservasActivity.this, "Error al cargar reservas", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Log.e("ProximasReservasActivity", "Error en la conexión", t);
//                Toast.makeText(ProximasReservasActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
