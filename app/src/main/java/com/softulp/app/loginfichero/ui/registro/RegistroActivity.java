package com.softulp.app.loginfichero.ui.registro;

import static android.Manifest.permission.CAMERA;

import android.Manifest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
//import com.softulp.app.loginfichero.Manifest;
import com.softulp.app.loginfichero.databinding.ActivityRegistroBinding;
import com.softulp.app.loginfichero.models.Usuario;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.ActivityResult;
public class RegistroActivity extends AppCompatActivity {
   private ActivityRegistroBinding binding;
    private  RegistroViewModel vm;
    private static final int REQUEST_IMAGE_CAPTURE=1;
    private Intent intent;
    private ActivityResultLauncher<Intent> arl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegistroBinding.inflate(getLayoutInflater());
        vm=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(RegistroViewModel.class);
        setContentView(binding.getRoot());


        abrirGaleria();

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
                //binding.imageView.setImageBitmap(usuario.getImg());
            }
        });
        vm.checkIntent(getIntent());

        abrirGaleria();



        binding.btnSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arl.launch(intent);
            }
        });
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
        binding.btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                  //  if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                 startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        configView();


    }

    public void configView(){


        validaPermisos();
        vm.getMutableFoto().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {

                binding.imageView.setImageBitmap(bitmap);

            }
        });
    }



    private boolean validaPermisos() {
        if((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{CAMERA},100);
        }

        return false;
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(RegistroActivity.this);
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(RegistroActivity.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{CAMERA},100);
            }
        });
        dialogo.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        vm.respuestaDeCamara(requestCode,resultCode,data,REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED){

            }else{
                solicitarPermisosManual();
            }
        }

    }


    private void abrirGaleria(){


        intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        arl=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                vm.recibirFoto(result);


            }
        });



    }

}