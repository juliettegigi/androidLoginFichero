package com.softulp.app.loginfichero.request;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.softulp.app.loginfichero.models.Usuario;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
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
            File archive=new File(context.getFilesDir(),nombreArchivo);
            FileInputStream fis=null;
            try {
                fis=new FileInputStream(archive);
                BufferedInputStream bis=new BufferedInputStream(fis);
                ObjectInputStream ois=new ObjectInputStream(bis);
                user = (Usuario) ois.readObject();

            } catch (FileNotFoundException e) {
                // el archivo no existe
                Toast.makeText(context,"Error de E/s noyfoud",Toast.LENGTH_LONG).show();
            } catch (EOFException e) {
                //el archivo está vacío
                Toast.makeText(context,"Error de E/s noyfoud",Toast.LENGTH_LONG).show();
            }

            catch (IOException e) {
                Toast.makeText(context,"Error de E/s",Toast.LENGTH_LONG).show();

            } catch (ClassNotFoundException e) {
                Toast.makeText(context,"Error al recuperar datos",Toast.LENGTH_LONG).show();
            }
            finally {
                if(fis!=null) {
                    try {
                        fis.close();
                    } catch (IOException e) {

                    }
                }
            }
            return user;

    }

    public static  Usuario login(Context context,String email,String pass){
        Usuario usuario=leer(context);
        if(usuario!=null && usuario.getDni()!=-1 && usuario.getEmail().equals(email) && usuario.getPass().equals(pass)){
            return usuario;
        }
        return null;
    }


    public static  Bitmap leerImagenBytes(Context context, String nombreFile){
        File archivo=new File(context.getFilesDir(),nombreFile);

        FileInputStream fi= null;
        Bitmap bitmap=null;
        try {

            fi = new FileInputStream(archivo);
            BufferedInputStream bi = new BufferedInputStream(fi);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int byteLeido;

            while ((byteLeido = bi.read()) != -1) {
                bos.write(byteLeido);
            }

            byte[] bytes = bos.toByteArray();
            fi.close();


            // Crear el Bitmap
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            return bitmap;
        } catch (FileNotFoundException e) {
            Toast.makeText(context,"Error al recuperar imageb",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context,"Error al recuperar imagen",Toast.LENGTH_LONG).show();
        }
        return bitmap;

    }

    public static  void guardarImageBytes(Context context, Bitmap bitmap, String nombreArchivo){
        if (bitmap == null) {
            return;
        }
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);//comprime el Bitmap en formato PNG y lo guarda en un ByteArrayOutputStream.
        byte [] b=baos.toByteArray();
        File archivo =new File(context.getFilesDir(),nombreArchivo);
        if(archivo.exists()){
            archivo.delete();
        }
        try {

            FileOutputStream fo=new FileOutputStream(archivo);
            BufferedOutputStream bo=new BufferedOutputStream(fo);
            bo.write(b);
            bo.flush();
            bo.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
