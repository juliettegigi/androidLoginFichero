package com.softulp.app.loginfichero.ui.registro;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.softulp.app.loginfichero.models.ExceptionUsuario;
import com.softulp.app.loginfichero.models.Usuario;
import com.softulp.app.loginfichero.request.ApiClient;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static android.app.Activity.RESULT_OK;
import androidx.activity.result.ActivityResult;
public class RegistroViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> mutableCerrar;
    private MutableLiveData<Usuario> mutableUsuario;
    private MutableLiveData<Bitmap> mutableFoto;
    

    public RegistroViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<Boolean> getMutableCerrar(){
        if(mutableCerrar==null){
            mutableCerrar=new MutableLiveData<>();
        }
        return mutableCerrar;
    }



    public LiveData<Usuario> getMutableUsuario(){
        if(mutableUsuario==null){
            mutableUsuario=new MutableLiveData<>();
        }
        return mutableUsuario;
    }

    public LiveData<Bitmap> getMutableFoto(){
        if(mutableFoto==null){
            mutableFoto=new MutableLiveData<>();
        }
        return mutableFoto;
    }
    public void guardar(CharSequence dni,CharSequence apellido,CharSequence nombre,CharSequence email,CharSequence pass){

        try
        {
            if(email.toString().isEmpty() && pass.toString().isEmpty()) {
                throw new ExceptionUsuario(3);
            }
            else if(email.toString().isEmpty()){
                throw new ExceptionUsuario(1);
            }
            else if(pass.toString().isEmpty()){
                throw new ExceptionUsuario(2);
            }
            if(dni.toString().isEmpty())
                dni="0";
            ApiClient.guardarImageBytes(getApplication(),mutableFoto.getValue(),"imagen1.png");
            Usuario usuario = new Usuario(Long.parseLong(dni.toString()), apellido.toString(), nombre.toString(), email.toString(), pass.toString(),"imagen1.png");
            ApiClient.guardar(getApplication(), usuario);
            mutableCerrar.setValue(true);
        }
        catch (NumberFormatException e){
            Toast.makeText(getApplication(), "El campo DNI es numérico", Toast.LENGTH_SHORT).show();
        }
        catch (ExceptionUsuario e){
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void checkIntent(Intent intent){

        if (intent != null) {
            if (intent.hasExtra("usuario")) {
                Usuario usuario = (Usuario) intent.getSerializableExtra("usuario");
                Bitmap img=ApiClient.leerImagenBytes(getApplication(), usuario.getImg());
                if(mutableFoto==null)mutableFoto=new MutableLiveData<>();
               mutableFoto.setValue(img);
                mutableUsuario.setValue(usuario);
            }
        }
    }

    public void respuestaDeCamara(int requestCode, int resultCode, @Nullable Intent data, int REQUEST_IMAGE_CAPTURE){
        Log.d("salida",requestCode+"");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Recupero los datos provenientes de la camara.
            Bundle extras = data.getExtras();
            //Casteo a bitmap lo obtenido de la camara.
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            mutableFoto.setValue(imageBitmap);
            try {
                baos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }





        }
    }

    public void recibirFoto(ActivityResult result) {
        if(result.getResultCode() == RESULT_OK){
            Intent data=result.getData();
            Uri uri=data.getData();
          //  mutableUri.setValue(uri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), uri);
                mutableFoto.setValue(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    }

}
