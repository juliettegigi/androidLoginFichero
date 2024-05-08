package com.softulp.app.loginfichero.models;

public class ExceptionUsuario extends Exception {
    private int codigo;
    public ExceptionUsuario(int codigo){
        super();
        this.codigo=codigo;
    }
    public ExceptionUsuario(String msg){
        super(msg);
    }
    @Override
    public String getMessage(){
        switch (codigo){
            case 1: return "EL campo email es obligatorio";
            case 2:return "El campo contraseña es obligatorio";
            case 3:return "El campo email y contraseña son obligatorios";
        }
        return null;
    }
}
