package com.softulp.app.loginfichero.ui.registro;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.softulp.app.loginfichero.models.ExceptionUsuario;
import com.softulp.app.loginfichero.models.Usuario;
import com.softulp.app.loginfichero.request.ApiClient;

public class RegistroViewModel extends AndroidViewModel {
    MutableLiveData<Boolean> mutableCerrar;
    MutableLiveData<Usuario> mutableUsuario;

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
            Usuario usuario = new Usuario(Long.parseLong(dni.toString()), apellido.toString(), nombre.toString(), email.toString(), pass.toString());
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
                mutableUsuario.setValue(usuario);
            }
        }
    }
}
