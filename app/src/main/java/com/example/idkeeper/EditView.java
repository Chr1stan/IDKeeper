package com.example.idkeeper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class EditView extends AppCompatActivity {//Comitted2

    private EditText etIdType, etIdCode, etNacionality, etExpDate;
    private Button btResize, btSave;

    private PdfDocument pdfDoc;
    private Bitmap bitmap;

    private String idtype;
    private String idCode;
    private String expDate;
    private String nacionality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view);
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
                    Toast.makeText(EditView.this, "BRrr", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(EditView.this, "Error retrieving bitmap", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btSave = findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idtype = etIdType.getText().toString().trim();
                idCode = etIdCode.getText().toString().trim();
                nacionality = etNacionality.getText().toString().trim();
                expDate = etExpDate.getText().toString().trim();
                setResult(3);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3 && resultCode == RESULT_OK) {
            Intent returndData = new Intent(getApplicationContext(), DocView.class);
            returndData.putExtra("ID Type", idtype);
            returndData.putExtra("ID Code", idCode);
            returndData.putExtra("Exp Date", expDate);
            returndData.putExtra("Nac", nacionality);
            setResult(RESULT_OK, returndData);
            startActivity(returndData);
            finish();
        }
    }
}