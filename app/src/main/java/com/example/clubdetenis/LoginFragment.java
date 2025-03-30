package com.example.clubdetenis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private TextView continueGuestTextView;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_first,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Iniciamos los elementos de la UI

        usernameEditText = view.findViewById(R.id.edittext_username);
        passwordEditText = view.findViewById(R.id.edittext_password);
        loginButton = view.findViewById(R.id.button_login);
        continueGuestTextView = view.findViewById(R.id.textview_continue_guest);

        //Ahora lo que vamos hacer es configurar un evento para poder iniciar sesion

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (!username.isEmpty() && !password.isEmpty()) {
                Toast.makeText(getContext(), "Iniciando la sesión correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Tienes que rellenar todos los campos", Toast.LENGTH_SHORT).show();

            }

        });

        continueGuestTextView.setOnClickListener(v ->
                Toast.makeText(getContext(), "Continuando como no socio....", Toast.LENGTH_SHORT).show()
        );

    }

    }
