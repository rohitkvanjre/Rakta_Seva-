package com.example.rakta_seva;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rakta_seva.data.AppDatabase;
import com.example.rakta_seva.data.DonorEntity;
import com.example.rakta_seva.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;
import java.util.Locale;

public class RegisterStep2Activity extends AppCompatActivity {

    private EditText etEmail, etPhone, etLocation, etLastDonation;
    private SwitchMaterial switchAvailable;
    private String name, gender, bloodGroup, userType;
    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step2);

        // Get data from Step 1
        name = getIntent().getStringExtra("name");
        age = getIntent().getIntExtra("age", 0);
        gender = getIntent().getStringExtra("gender");
        bloodGroup = getIntent().getStringExtra("bloodGroup");
        userType = getIntent().getStringExtra("userType");
        String userTypeTitle = getIntent().getStringExtra("userTypeTitle");
        String userTypeDesc = getIntent().getStringExtra("userTypeDesc");

        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etLocation = findViewById(R.id.et_location);
        etLastDonation = findViewById(R.id.et_last_donation);
        switchAvailable = findViewById(R.id.switch_available);
        MaterialButton btnContinue = findViewById(R.id.btn_continue);
        ImageView btnBack = findViewById(R.id.btn_back);
        TextView tvUserTypeTitle = findViewById(R.id.tv_user_type_title);
        TextView tvUserTypeDesc = findViewById(R.id.tv_user_type_desc);

        // Display user type from Step 1
        if (userTypeTitle != null) tvUserTypeTitle.setText(userTypeTitle);
        if (userTypeDesc != null) tvUserTypeDesc.setText(userTypeDesc);

        btnBack.setOnClickListener(v -> finish());

        // Date picker for last donation
        etLastDonation.setOnClickListener(v -> showDatePicker());

        btnContinue.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String lastDonation = etLastDonation.getText().toString().trim();
            boolean available = switchAvailable.isChecked();

            // Validation
            if (email.isEmpty()) {
                etEmail.setError("Email is required");
                etEmail.requestFocus();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Please enter a valid email");
                etEmail.requestFocus();
                return;
            }

            if (phone.isEmpty()) {
                etPhone.setError("Phone number is required");
                etPhone.requestFocus();
                return;
            }

            if (phone.length() != 10) {
                etPhone.setError("Enter a valid 10-digit number");
                etPhone.requestFocus();
                return;
            }

            if (location.isEmpty()) {
                etLocation.setError("Location is required");
                etLocation.requestFocus();
                return;
            }

            // Save to SharedPreferences
            SessionManager session = new SessionManager(this);
            session.saveLoginSession(name, phone, bloodGroup, age, gender, email,
                    location, lastDonation, false, available);
            session.setUserType(userType);

            // Save to Room Database
            DonorEntity donor = new DonorEntity();
            donor.name = name;
            donor.bloodGroup = bloodGroup;
            donor.age = age;
            donor.gender = gender;
            donor.phone = phone;
            donor.email = email;
            donor.location = location;
            donor.latitude = 28.630;
            donor.longitude = 77.209;
            donor.lastDonationDate = lastDonation;
            donor.isAvailable = available;
            donor.isVerified = false;
            donor.userType = userType;

            AppDatabase.getInstance(this).donorDao().insertDonor(donor);

            // Navigate to OTP verification
            Intent intent = new Intent(RegisterStep2Activity.this, OtpActivity.class);
            intent.putExtra("phone", phone);
            startActivity(intent);
        });
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String date = String.format(Locale.US, "%02d/%02d/%04d",
                            month + 1, dayOfMonth, year);
                    etLastDonation.setText(date);
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }
}
