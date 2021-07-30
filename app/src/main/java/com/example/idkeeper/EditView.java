package com.example.idkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
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

public class EditView extends AppCompatActivity {//Comitted

    private EditText etIdType, etIdCode, etNacionality, etExpDate;
    private Button btResize, btSave;

    private PdfDocument pdfDoc;
private databaseHelper2 db2;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view);

        db2 = new databaseHelper2(this);
        Cursor cursor2 = db2.getID();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        etIdCode = (findViewById(R.id.etIdCode));
        etIdType = (findViewById(R.id.etIdType));
        etNacionality = (findViewById(R.id.etNacionality));
        etExpDate = (findViewById(R.id.etExpDate));

        btResize = findViewById(R.id.btResize);
        btResize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(EditView.this, ""+cursor2.getString(5), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(EditView.this, "Error retrieving bitmap", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


        btSave = findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idtype = etIdType.getText().toString().trim();
                String idCode = etIdCode.getText().toString().trim();
                String nacionality = etNacionality.getText().toString().trim();
                String expDate = etExpDate.getText().toString().trim();

//                Intent intent = getIntent();
//                String bitmapString = intent.getStringExtra("shit");
//                String bitmapString;
//                try {
//                    bitmap = getIntent().getParcelableExtra("bitmap");
//                    bitmapString = bitmap.toString();
//                }catch (Exception e){bitmapString = "null";}
                boolean addID = db2.addID(idtype, idCode, nacionality, expDate, "shit");
                if (addID) {
                    //convertToPDF(bitmap);
                    Toast.makeText(EditView.this, "Saved Sucessfuly", Toast.LENGTH_SHORT).show();
                    Intent docView_intent = new Intent(getApplicationContext(), DocView.class);
                    startActivity(docView_intent);
                } else {
                    Toast.makeText(EditView.this, "Error while saving file", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void convertToPDF(Bitmap bitmap) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            pdfDoc.close();
        }
    }
}