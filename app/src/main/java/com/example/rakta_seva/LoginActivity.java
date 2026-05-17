package com.example.rakta_seva;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPhone = findViewById(R.id.et_phone);
        MaterialButton btnSendOtp = findViewById(R.id.btn_send_otp);
        MaterialButton btnRegisterNew = findViewById(R.id.btn_register_new);
        ImageView btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        btnSendOtp.setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();

            if (phone.isEmpty()) {
                Toast.makeText(this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.length() != 10) {
                Toast.makeText(this, "Please enter a valid 10-digit number", Toast.LENGTH_SHORT).show();
                return;
            }

            com.example.rakta_seva.data.DonorEntity donor = com.example.rakta_seva.data.AppDatabase.getInstance(this).donorDao().getDonorByPhone(phone);
            if (donor != null || phone.equals("9876543210")) {
                Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
            } else {
                Toast.makeText(this, "User not found. Please register.", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegisterNew.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
