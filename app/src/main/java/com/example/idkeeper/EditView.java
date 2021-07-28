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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class EditView extends AppCompatActivity {

    private EditText etIdType, etIdCode, etNacionality, etExpDate;
    private Button btResize, btSave;

    private PdfDocument pdfDoc;
    private databaseHelper2 db;
    private Bitmap bitmap;

    private ImageView imageView1, imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view);

        if(getSupportActionBar() != null){getSupportActionBar().hide();}

        etIdCode = (findViewById(R.id.etIdCode));
        etIdType = (findViewById(R.id.etIdType));
        etNacionality = (findViewById(R.id.etNacionality));
        etExpDate = (findViewById(R.id.etExpDate));

        btResize = findViewById(R.id.btResize);
        btResize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditView.this, "To crop or resize the image", Toast.LENGTH_SHORT).show();
            }
        });

        db = new databaseHelper2(this);

        btSave = findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idtype = etIdType.getText().toString().trim();
                String idCode = etIdCode.getText().toString().trim();
                String nacionality = etNacionality.getText().toString().trim();
                String expDate = etExpDate.getText().toString().trim();

                //Intent intent = getIntent();
                //bitmap = intent.getParcelableExtra("bitmap");
                boolean addID = db.addID(idtype, idCode, nacionality, expDate);
                if (addID){
                    //convertToPDF(bitmap);
                    Toast.makeText(EditView.this, "Saved Sucessfuly", Toast.LENGTH_SHORT).show();
                    Intent docView_intent = new Intent(getApplicationContext(), DocView.class);
                    startActivity(docView_intent);
                }else{
                    Toast.makeText(EditView.this, "Error while saving file", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void convertToPDF(Bitmap bitmap){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            pdfDoc = new PdfDocument();
            PdfDocument.PageInfo pInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();

            PdfDocument.Page page = pdfDoc.startPage(pInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#F4AE59"));
            canvas.drawPaint(paint);

            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
            paint.setColor(Color.BLUE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            pdfDoc.finishPage(page);

            File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

            try {
                File file = File.createTempFile("converted", ".pdf", dir);
                FileOutputStream fileOut = new FileOutputStream(file);
                pdfDoc.writeTo(fileOut);
            } catch (Exception e) {e.printStackTrace();}
            pdfDoc.close();
        }
    }
}