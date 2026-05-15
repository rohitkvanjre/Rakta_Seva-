package com.example.rakta_seva;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        MaterialButton btnLogin = findViewById(R.id.btn_login);
        MaterialButton btnRegister = findViewById(R.id.btn_register);

        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(LandingActivity.this, LoginActivity.class));
        });

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(LandingActivity.this, RegisterActivity.class));
        });
    }
}
