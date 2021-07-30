package com.example.idkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{


    private EditText etPin;
    private TextView tvLogin;
    private Button btSignUp, btLogin;

    private databaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportActionBar() != null){getSupportActionBar().hide();}

        tvLogin = findViewById(R.id.tvLogin);

        db = new databaseHelper(this);
        Cursor cursor = db.getUser();

        Intent docViewIntent = new Intent(getApplicationContext(), DocView.class);
        etPin = (EditText)findViewById(R.id.etPin);
        etPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String pin = etPin.getText().toString().trim();
                    if (cursor.getCount() != 0) {
                        if (!pin.isEmpty()) {
                            cursor.moveToLast();
                            if (pin.equalsIgnoreCase(cursor.getString(3).trim())) {
                                startActivity(docViewIntent);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "enter the pin", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (!pin.isEmpty())
                            if (pin.equalsIgnoreCase("1234") || pin.equalsIgnoreCase("0000")) {
                                startActivity(docViewIntent);
                            }
                    }
                }catch (Exception e){}
            }
        });

        btLogin = findViewById(R.id.btLogIn);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = etPin.getText().toString().trim();
                if(!pin.isEmpty()){
                    cursor.moveToLast();
                    if(pin.equalsIgnoreCase(cursor.getString(3).trim())){
                        startActivity(docViewIntent);
                    }else {
                        Toast.makeText(MainActivity.this, "incorrect Pin", Toast.LENGTH_SHORT).show();
                        etPin.setText("");
                    }
                }else{
                    Toast.makeText(MainActivity.this, "enter the pin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent signUpIntent = new Intent(getApplicationContext(), SignUp.class);
        btSignUp = (Button)findViewById(R.id.btSignUp);
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{startActivity(signUpIntent);}catch(Exception e){}
                Toast.makeText(MainActivity.this, "Fill in the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}