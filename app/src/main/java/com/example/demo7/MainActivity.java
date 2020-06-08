package com.example.demo7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.demo7.Dto.Trabajo;
import com.example.demo7.Dto.TrabajoDto;
import com.example.demo7.Repository.TrabajoRepository;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File file = new File(getFilesDir() , "subdirectorio");

        if(file.exists()){
            Log.d("infoAppArc","directorio existe");
        }else{
            file.mkdir();
            Log.d("infoAppArc","directorio creado");
        }


        //listarTrabajos();

       /* String[] archivos = fileList();
        for(String file: archivos){
            Log.d("infoAppArc",file);
            if(file.equals("listaTrabajosBytesLunes")){
                deleteFile("listaTrabajosBytesLunes");
                Log.d("infoAppArc","Borrado de archivo");
            }
        }

        archivos = fileList();
        for(String file: archivos){
            Log.d("infoAppArc",file);
        }*/

        //listarArchivos();
        //leerArchivoTexto();
        //leerArchivoBytes();
        //validarPermisos();
/*
        TrabajoRepository trabajoRepository = new TrabajoRepository(getApplication());
        trabajoRepository.getListaTrabajos().observe(this, new Observer<List<Trabajo>>() {
            @Override
            public void onChanged(List<Trabajo> trabajos) {
                Log.d("infoApp","estamos en la vista!");
                for(Trabajo t :trabajos){
                    Log.d("infoApp",t.getJobId());
                }
            }
        });
        trabajoRepository.listarTrabajos();*/

    }

    public void gestionSP() {
        SharedPreferences sharedPreferences = getSharedPreferences("archivo1", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences.edit();

        editor2.putString("nombre", "Cesar");

        editor2.apply();


        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("dni", "12345678");

        editor.apply();

        //leerPref();
        leerSharedPref();

    }

    public void leerSharedPref() {
        SharedPreferences preferences = getSharedPreferences("archivo1", MODE_PRIVATE);
        String dni = preferences.getString("nombre", "no existe dni");

        if (dni.equals("no existe dni")) {
            Log.d("infoApp", "No existe dni");
        } else {
            Log.d("infoApp", "Si existe dni: " + dni);
        }
    }

    public void leerPref() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String dni = preferences.getString("dni", "no existe dni");

        if (dni.equals("no existe dni")) {
            Log.d("infoApp", "No existe dni");
        } else {
            Log.d("infoApp", "Si existe dni: " + dni);
        }
    }

    public void descargarImagenDownloadManager() {
        String fileName = "pucp.jpg";
        String endPoint = "http://192.168.1.14:9000/get-image";

        Uri downloadUri = Uri.parse(endPoint);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle(fileName);
        request.setMimeType("image/jpeg");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                File.separator + fileName);

        DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        dm.enqueue(request);
    }

    public void descargarImagenConVolley() {
        String endPoint = "http://192.168.1.14:9000/get-image";

        ImageRequest imageRequest = new ImageRequest(endPoint,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        procesarImagen(response);
                    }
                },
                0, 0, ImageView.ScaleType.CENTER_CROP, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(imageRequest);

    }

    public void procesarImagen(Bitmap bitmap) {
        String name = "pucp";
        OutputStream fos = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = MainActivity.this.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name + ".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                fos = resolver.openOutputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            String imagesDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            File image = new File(imagesDir, name + ".jpg");
            try {
                fos = new FileOutputStream(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        try {
            if (fos != null) fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int codigoPermisoWriteReadSD = 1;

    public void validarPermisos() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            Log.d("infoApp", "tenemos permisos");
            //descargarImagenDownloadManager();
            descargarImagenConVolley();
        } else {
            Log.d("infoApp", "No tenemos permisos");

            String[] arregloPermisos = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

            ActivityCompat.requestPermissions(
                    this,
                    arregloPermisos,
                    codigoPermisoWriteReadSD);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == codigoPermisoWriteReadSD) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("infoApp", "Sí aceptó los permisos");
            } else {
                Log.d("infoApp", "No aceptó los permisos");
            }
        }
    }

    public void guardarComoTextoSD(Trabajo[] arregloTrabajo) {

        if (existeMemoriaSdReadWrite()) {

            String fileName = "listaTrabajosJson";

            File file = new File(getExternalFilesDir(null), fileName);

            try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                 FileWriter fileWriter = new FileWriter(fileOutputStream.getFD());) {
                Gson gson = new Gson();
                String listaComoJson = gson.toJson(arregloTrabajo);
                fileWriter.write(listaComoJson);
                Log.d("infoApp", "Guardado exitoso");
            } catch (IOException e) {
                Log.d("infoApp", "Error al guardar");
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No existe memoria SD", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean existeMemoriaSdReadWrite() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public void leerArchivoBytes() {
        String fileName = "listaTrabajosBytes";

        try (FileInputStream fileInputStream = openFileInput(fileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            Trabajo[] arregloTrabajo = (Trabajo[]) objectInputStream.readObject();

            for (Trabajo t : arregloTrabajo) {
                Log.d("infoApp", t.getJobTitle());
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void leerArchivoTextoSubcarpeta() {

        String fileName = "listaTrabajosJson";

        File subdir = new File(getFilesDir(),"subdirectorio");
        File archivo = new File(subdir,fileName);


        try (FileInputStream fileInputStream = new FileInputStream(archivo);

             FileReader fileReader = new FileReader(fileInputStream.getFD());) {
            Gson gson = new Gson();

            try (BufferedReader bufferedReader = new BufferedReader(fileReader);) {


                String line = bufferedReader.readLine();

                Trabajo[] arregloTrabajo = gson.fromJson(line, Trabajo[].class);

                // guardarComoTextoSD(arregloTrabajo);

                for (Trabajo t : arregloTrabajo) {
                    Log.d("infoApp", t.getJobTitle());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void leerArchivoTexto() {
        String fileName = "listaTrabajosJson";

        try (FileInputStream fileInputStream = openFileInput(fileName);

             FileReader fileReader = new FileReader(fileInputStream.getFD());) {
            Gson gson = new Gson();

            try (BufferedReader bufferedReader = new BufferedReader(fileReader);) {


                String line = bufferedReader.readLine();

                Trabajo[] arregloTrabajo = gson.fromJson(line, Trabajo[].class);

                // guardarComoTextoSD(arregloTrabajo);

                for (Trabajo t : arregloTrabajo) {
                    Log.d("infoApp", t.getJobTitle());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listarArchivos() {
        String[] listaArchivos = fileList();

        for (String archivo : listaArchivos) {
            Log.d("infoApp", archivo);
        }
    }

    public void guardarComoBytes(Trabajo[] arregloTrabajo) {

        String fileName = "listaTrabajosBytesLunes";

        try (FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);

             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(arregloTrabajo);

            Log.d("infoApp", "Guardado exitoso");
        } catch (IOException e) {
            Log.d("infoApp", "Error al guardar");
            e.printStackTrace();
        }
    }

    public void guardarComoTexto(Trabajo[] arregloTrabajo) {

        String fileName = "listaTrabajosJson";

        try (FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);

             FileWriter fileWriter = new FileWriter(fileOutputStream.getFD());) {

            Gson gson = new Gson();
            String listaComoJson = gson.toJson(arregloTrabajo);
            fileWriter.write(listaComoJson);

            Log.d("infoApp", "Guardado exitoso");
        } catch (IOException e) {
            Log.d("infoApp", "Error al guardar");
            e.printStackTrace();
        }


    }

    public void guardarComoTextoSubDir(Trabajo[] arregloTrabajo) {

        String fileName = "listaTrabajosJson";

        try (FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);

             FileWriter fileWriter = new FileWriter(fileOutputStream.getFD());) {

            Gson gson = new Gson();
            String listaComoJson = gson.toJson(arregloTrabajo);
            fileWriter.write(listaComoJson);

            Log.d("infoApp", "Guardado exitoso");
        } catch (IOException e) {
            Log.d("infoApp", "Error al guardar");
            e.printStackTrace();
        }


    }

    public void listarTrabajos() {

        String url = "http://ec2-54-165-73-192.compute-1.amazonaws.com:9000/listar/trabajos";

        StringRequest request = new StringRequest(StringRequest.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        TrabajoDto dto = gson.fromJson(response, TrabajoDto.class);
                        Trabajo[] arregloTrabajo = dto.getTrabajos();
                        //TrabajoRepository trabajoRepository = new TrabajoRepository(MainActivity.this.getApplication());
                        for (Trabajo t : arregloTrabajo) {
                            //trabajoRepository.guardarTrabajo(t);
                            Log.d("infoApp", t.getJobTitle());
                        }

                        // trabajoRepository.listarTrabajos();


                        //guardarComoTexto(arregloTrabajo);
                        guardarComoBytes(arregloTrabajo);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("api-key", "2RmQ3RyQLvvQJcv8T94J");
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }
}
