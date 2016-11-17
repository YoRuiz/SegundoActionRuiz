package com.example.jajimenez.segundoactionruiz;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button boton;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boton = (Button) findViewById(R.id.botonCamara);
        boton.setOnClickListener(this);
    }

    public void verifyStoragePermissions(Activity activity) {
        //Comprueba si tiene permisos de escritura
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            //Si no los tiene se los pide al usuario
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onClick(View v) {


        llamaCamara();


    }

    public void llamaCamara() {

        //Creamos el Intent para llamar a la Camara
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        //Creamos una carpeta en la memoria del teléfono
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "PhotoFace");
        boolean b = imagesFolder.mkdir();//Crea un directorio para las imágenes de nuestra app

        //Añadimos el nombre de la imagen
        File image = new File(imagesFolder, "fotico.jpg");
        Uri uriSavedImage = Uri.fromFile(image);

        //Le decimos al Intent que queremos grabar la imagen
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        //Lanzamos la aplicación de la cámara con retorno (forResult)
        startActivityForResult(cameraIntent, 1);

    }

    public void llamaInstagram() {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
        String s = Environment.getExternalStorageDirectory() + "/PhotoFace/fotico.jpg";
        if (intent != null)
        {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
//            shareIntent.setPackage("com.instagram.android");
            try {
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), s, "I am Happy", "Share happy !")));
            } catch (FileNotFoundException e) {
                System.out.println(e);
                e.printStackTrace();
            }
            shareIntent.setType("image/jpeg");

            startActivity(shareIntent);
        }/*
        else
        {
            // bring user to the market to download the app.
            // or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id="+"com.instagram.android"));
            startActivity(intent);
        }*/


    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        int i=0;
        i++;
        //Comprobamos que se ha hecho la foto
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            llamaInstagram();
        }
    }
}
