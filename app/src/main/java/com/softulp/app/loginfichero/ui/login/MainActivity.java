package com.softulp.app.loginfichero.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.softulp.app.loginfichero.R;
import com.softulp.app.loginfichero.databinding.ActivityMainBinding;
import com.softulp.app.loginfichero.ui.registro.RegistroActivity;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MainViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        vm=new ViewModelProvider(this).get(MainViewModel.class);
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        vm.getMutableMsgError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvMsgError.setText(s);
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.validar(binding.etUsuario.getText().toString(),
                        binding.etPass.getText().toString());

            }
        });

        binding.tvRegistrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.tvMsgError.setText("");
    }
}