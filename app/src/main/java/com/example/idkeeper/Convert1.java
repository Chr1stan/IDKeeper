package com.example.idkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class Convert1 extends AppCompatActivity {

    private Button btConvert;
    private ImageView imageView;

    private PdfDocument pdfDoc, pdfDocument;
    private Bitmap bitmap;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert1);

        imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
        
        btConvert = findViewById(R.id.btConvert);
        btConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }
    public void convert(Bitmap bitmap){
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
            Toast.makeText(this, "DONE!", Toast.LENGTH_SHORT).show();
        }
    }
}