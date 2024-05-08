package com.softulp.app.loginfichero.ui.login;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.softulp.app.loginfichero.models.Usuario;
import com.softulp.app.loginfichero.request.ApiClient;
import com.softulp.app.loginfichero.ui.registro.RegistroActivity;

public class MainViewModel extends AndroidViewModel {

    MutableLiveData<String> mutableMsgError;
    public MainViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getMutableMsgError(){
        if(mutableMsgError==null)
            mutableMsgError=new MutableLiveData<>();
        return  mutableMsgError;
    }

    public void validar(String email,String pass){
        Usuario usuario= ApiClient.login(getApplication(),email,pass);
        if(usuario==null) {
            mutableMsgError.setValue("Email o contraseña incorrectos.");
            return;
        }
        Intent intent=new Intent(getApplication(), RegistroActivity.class);
        intent.putExtra("usuario",usuario);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }
}
