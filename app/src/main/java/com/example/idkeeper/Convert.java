package com.example.idkeeper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Convert extends AppCompatActivity {

    private ImageView imageView;

    private Button btGallery, btTakePhoto, btConvert;

    private String currentPhotoPath;

    private File dir, imageFile;
    private Uri imageUri;

    //private Bitmap bitmap;

    private databaseHelper2 db;

    private PdfDocument pdfDoc;

    private String currerntPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        if(getSupportActionBar() != null){getSupportActionBar().hide();}

        db = new databaseHelper2(this);

        imageView = findViewById(R.id.imageView);

        //button to call the method to open gallery
        btGallery = findViewById(R.id.btGallery);
        btGallery.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {openGallery();}});

        findViewById(R.id.btTakePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File imagefile = File.createTempFile("image", ".jpg", dir);

                    currerntPhotoPath = imagefile.getAbsolutePath();

                    Uri imageUri = FileProvider.getUriForFile(Convert.this, "com.example.idkeeper.fileprovider", imagefile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent , 1);
                } catch (IOException e) {e.printStackTrace();}
            }
        });
        /*button to call the method to take photo
        btTakePhoto = findViewById(R.id.btTakePhoto);
        btTakePhoto.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {takePhoto();}});*/

        //button to call the method to convert the photo

        /*btConvert = findViewById(R.id.btConvert);
        btConvert.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {convertToPdf(bitmap);}});*/
    }

    public void openGallery(){
        try{Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentGallery, 2);}catch (Exception e){}
    }

    /*public void takePhoto(){
        dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try{
            imageFile = File.createTempFile("id", ".jpg", dir);
            String fileName = imageFile.getName();

            currentPhotoPath = imageFile.getAbsolutePath();

            imageUri = FileProvider.getUriForFile(Scan.this, "com.example.idkeeper.fileprovider", imageFile);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, 1);
        }catch (Exception e){e.printStackTrace();}
    }*/

    //this method is called when click "OK" after taking or choosing a photo
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent returnIntent) {
        super.onActivityResult(requestCode, resultCode, returnIntent);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);

            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                pdfDoc = new PdfDocument();
                PdfDocument.PageInfo pInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();

                PdfDocument.Page page = pdfDoc.startPage(pInfo);
                Canvas canvas = page.getCanvas();
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#FFFFFF"));
                canvas.drawPaint(paint);

                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                paint.setColor(Color.BLUE);
                canvas.drawBitmap(bitmap, 0, 0, null);

                pdfDoc.finishPage(page);

                File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                if (!dir.exists())
                    dir.mkdir();

                Random rand = new Random();
                int n = 10000;
                n = rand.nextInt(n);
                File file = new File(dir, "id" + n + "_converted.pdf");

                try {
                    FileOutputStream fileOut = new FileOutputStream(file);
                    pdfDoc.writeTo(fileOut);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pdfDoc.close();
            }
        }
    }

    /*public void convertToPdf(Bitmap bitmap){

        boolean res = false;
        try{res = db.addID(bitmap.toString());}catch (Exception e){}

        if (res) {
            try{convertToPdf(bitmap);}catch (Exception e){}
            Toast.makeText(Scan.this, "Added ID to database", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(Scan.this, "Failed adding ID to db ", Toast.LENGTH_SHORT).show();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            pdfDoc = new PdfDocument();
            PdfDocument.PageInfo pInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();

            PdfDocument.Page page = pdfDoc.startPage(pInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#FFFFFF"));
            canvas.drawPaint(paint);

            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
            paint.setColor(Color.BLUE);
            canvas.drawBitmap(bitmap, 0, 0, null);

            pdfDoc.finishPage(page);

            File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (!dir.exists())
                dir.mkdir();

            Random rand = new Random();
            int n = 10000;
            n = rand.nextInt(n);
            File file = new File(dir, "id"+n+"_converted.pdf");

            try{
                FileOutputStream fileOut = new FileOutputStream(file);
                pdfDoc.writeTo(fileOut);
            }catch (Exception e){e.printStackTrace();}
            pdfDoc.close();
        }
    }*/
}