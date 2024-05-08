package com.softulp.app.loginfichero.request;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.softulp.app.loginfichero.models.Usuario;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ApiClient {
   private static String nombreArchivo;
   static {
       nombreArchivo="archivo1.dat";
   }

    public static void guardar(Context context, Usuario user){

        File archivo=new File(context.getFilesDir(),nombreArchivo);
        ObjectOutputStream oos = null;
        try {

            FileOutputStream fos = new FileOutputStream(archivo);
            BufferedOutputStream bos=new BufferedOutputStream(fos);
            oos=new ObjectOutputStream(bos);
            oos.writeObject(user);
            Log.d("Salida","qqqqqqq");
            bos.flush();
            oos.close();

        } catch (FileNotFoundException e) {
            Toast.makeText(context,"Error al acceder al archivo",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context,"Error al acceder al archivo",Toast.LENGTH_LONG).show();
        }finally { if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static Usuario leer(Context context){
            Usuario user=null;
            File archivo=new File(context.getFilesDir(),nombreArchivo);

            FileInputStream fis=null;
            try {

                fis=new FileInputStream(archivo);
                BufferedInputStream bis=new BufferedInputStream(fis);
                Log.d("salida","etrro553yy");
                ObjectInputStream ois=new ObjectInputStream(bis);
                Log.d("salida","etrro553");
                while((bis.available() > 0)){

                    user=(Usuario) ois.readObject();
                    String nombre=user.getNombre();
                    String apellido= user.getApellido();
                    long dni=user.getDni();
                    String email=user.getEmail();
                    String pass=user.getPass();
                }
                return user;
            } catch (FileNotFoundException e) {
                Toast.makeText(context,"Error de E/s",Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(context,"Error de E/s",Toast.LENGTH_LONG).show();

            } catch (ClassNotFoundException e) {
                Toast.makeText(context,"Error al recuperar datos",Toast.LENGTH_LONG).show();
            }
            finally {
                if(fis!=null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return user;

    }

    public static  Usuario login(Context context,String email,String pass){
        Usuario usuario=leer(context);
        Log.d("salida",usuario.getEmail());
        Log.d("salida",email);
        if(usuario.getDni()!=-1 && usuario.getEmail().equals(email) && usuario.getPass().equals(pass)){
            return usuario;
        }
        return null;
    }


    private static class AppendingObjectOutputStream extends ObjectOutputStream {
        public AppendingObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            // No escribe la cabecera del objeto cada vez
            reset();
        }
    }
}
