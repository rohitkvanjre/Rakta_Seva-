package com.example.rakta_seva;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rakta_seva.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

public class OtpActivity extends AppCompatActivity {

    private EditText otp1, otp2, otp3, otp4;
    private TextView tvPhoneNumber, tvResend;
    private String phoneNumber;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        phoneNumber = getIntent().getStringExtra("phone");

        otp1 = findViewById(R.id.otp_1);
        otp2 = findViewById(R.id.otp_2);
        otp3 = findViewById(R.id.otp_3);
        otp4 = findViewById(R.id.otp_4);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvResend = findViewById(R.id.tv_resend);
        MaterialButton btnVerify = findViewById(R.id.btn_verify);
        ImageView btnBack = findViewById(R.id.btn_back);

        if (phoneNumber != null) {
            tvPhoneNumber.setText("OTP sent to +91 " + phoneNumber);
        }

        btnBack.setOnClickListener(v -> finish());

        // Auto-focus next OTP box
        setupOtpAutoFocus(otp1, otp2);
        setupOtpAutoFocus(otp2, otp3);
        setupOtpAutoFocus(otp3, otp4);
        setupOtpAutoFocus(otp4, null);

        btnVerify.setOnClickListener(v -> {
            String otp = otp1.getText().toString() + otp2.getText().toString()
                    + otp3.getText().toString() + otp4.getText().toString();

            if (otp.length() != 4) {
                Toast.makeText(this, "Please enter complete OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            if (otp.equals("1234")) {
                SessionManager session = new SessionManager(this);
                session.saveLoginSession(
                        "Rajesh Kumar",
                        "9876543210",
                        "O-",
                        28,
                        "Male",
                        "rajesh.kumar@email.com",
                        "Sector 12, New Delhi",
                        "March 15, 2026",
                        true,
                        true
                );

                Intent intent = new Intent(OtpActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid OTP. Try 1234", Toast.LENGTH_SHORT).show();
            }
        });

        // Start resend countdown
        startResendCountdown();
    }

    private void setupOtpAutoFocus(EditText current, EditText next) {
        current.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && next != null) {
                    next.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void startResendCountdown() {
        tvResend.setEnabled(false);
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvResend.setText("Resend OTP in " + (millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                tvResend.setText("Resend OTP");
                tvResend.setEnabled(true);
                tvResend.setOnClickListener(v -> {
                    Toast.makeText(OtpActivity.this, "OTP resent!", Toast.LENGTH_SHORT).show();
                    startResendCountdown();
                });
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
