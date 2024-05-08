package com.softulp.app.loginfichero.ui.registro;

import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.softulp.app.loginfichero.databinding.ActivityRegistroBinding;
import com.softulp.app.loginfichero.models.Usuario;

public class RegistroActivity extends AppCompatActivity {
   ActivityRegistroBinding binding;
    RegistroViewModel vm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegistroBinding.inflate(getLayoutInflater());
        vm=new ViewModelProvider(this).get(RegistroViewModel.class);
        setContentView(binding.getRoot());




        vm.getMutableCerrar().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                finish();
            }
        });

        vm.getMutableUsuario().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                binding.etDni.setText(String.valueOf(usuario.getDni()));
                binding.etApellido.setText(usuario.getApellido());
                binding.etNombre.setText(usuario.getNombre());
                binding.etEmail.setText(usuario.getEmail());
                binding.etPass2.setText(usuario.getPass());
            }
        });
        vm.checkIntent(getIntent());
        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.guardar(binding.etDni.getText(),
                        binding.etApellido.getText(),
                        binding.etNombre.getText(),
                        binding.etEmail.getText(),
                        binding.etPass2.getText());
            }
        });


    }

}