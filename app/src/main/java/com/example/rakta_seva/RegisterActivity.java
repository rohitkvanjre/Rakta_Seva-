package com.example.rakta_seva;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etAge;
    private Spinner spinnerGender, spinnerBloodGroup;
    private RadioGroup rgUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step1);

        etName = findViewById(R.id.et_name);
        etAge = findViewById(R.id.et_age);
        spinnerGender = findViewById(R.id.spinner_gender);
        spinnerBloodGroup = findViewById(R.id.spinner_blood_group);
        rgUserType = findViewById(R.id.rg_user_type);
        MaterialButton btnNext = findViewById(R.id.btn_next);
        ImageView btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        // Setup Gender Spinner
        String[] genders = {"Select", "Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, genders);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        // Setup Blood Group Spinner
        String[] bloodGroups = {"Select blood group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, bloodGroups);
        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodGroup.setAdapter(bloodAdapter);

        btnNext.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();
            String gender = spinnerGender.getSelectedItem().toString();
            String bloodGroup = spinnerBloodGroup.getSelectedItem().toString();

            // Validation
            if (name.isEmpty()) {
                etName.setError("Name is required");
                etName.requestFocus();
                return;
            }

            if (ageStr.isEmpty()) {
                etAge.setError("Age is required");
                etAge.requestFocus();
                return;
            }

            int age = Integer.parseInt(ageStr);
            if (age < 18 || age > 65) {
                etAge.setError("Age must be between 18 and 65");
                etAge.requestFocus();
                return;
            }

            if (gender.equals("Select")) {
                Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
                return;
            }

            if (bloodGroup.equals("Select blood group")) {
                Toast.makeText(this, "Please select blood group", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedUserType = rgUserType.getCheckedRadioButtonId();
            if (selectedUserType == -1) {
                Toast.makeText(this, "Please select what you want to do", Toast.LENGTH_SHORT).show();
                return;
            }

            String userType;
            String userTypeTitle;
            String userTypeDesc;
            if (selectedUserType == R.id.rb_donate) {
                userType = "donor";
                userTypeTitle = "Donate Blood Only";
                userTypeDesc = "I want to help others by donating blood";
            } else if (selectedUserType == R.id.rb_request) {
                userType = "requester";
                userTypeTitle = "Request Blood Only";
                userTypeDesc = "I may need blood in emergencies";
            } else {
                userType = "both";
                userTypeTitle = "Both Donate & Request";
                userTypeDesc = "I want to donate and may need blood too";
            }

            // Pass data to Step 2
            Intent intent = new Intent(RegisterActivity.this, RegisterStep2Activity.class);
            intent.putExtra("name", name);
            intent.putExtra("age", age);
            intent.putExtra("gender", gender);
            intent.putExtra("bloodGroup", bloodGroup);
            intent.putExtra("userType", userType);
            intent.putExtra("userTypeTitle", userTypeTitle);
            intent.putExtra("userTypeDesc", userTypeDesc);
            startActivity(intent);
        });
    }
}
