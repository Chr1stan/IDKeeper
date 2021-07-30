package com.example.idkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    private Button btSignUp;
    private TextView tvAppName;
    private EditText etName, etPin, etPin2;
    private databaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tvAppName = findViewById(R.id.tvAppName);
        etName = findViewById(R.id.etName);
        etPin = (EditText) findViewById(R.id.etPin);
        etPin2 = (EditText) findViewById(R.id.etPin2);

        db = new databaseHelper(this);

        Intent logIntent = new Intent(getApplicationContext(), MainActivity.class);
        btSignUp = findViewById(R.id.btSignUp);
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String fullName[] = etName.getText().toString().split(" ");

                    String name = fullName[0];
                    String surname = fullName[1];

                    if (etPin.getText().toString().trim().equalsIgnoreCase(etPin2.getText().toString().trim())) {
                        String pin = etPin.getText().toString().trim();
                        if (name.isEmpty() || surname.isEmpty()) {
                            Toast.makeText(SignUp.this, "Enter full Name", Toast.LENGTH_SHORT).show();
                        } else {
                            boolean insert = db.addUser(name, surname, pin);
                            if (insert) {
                                startActivity(logIntent);
                                Toast.makeText(SignUp.this, "User added", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUp.this, "Error adding user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        etPin.setText("");
                        etPin2.setText("");
                        Toast.makeText(SignUp.this, "The two pin doesn't match", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}