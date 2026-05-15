package com.example.rakta_seva.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.rakta_seva.R;
import com.example.rakta_seva.data.AppDatabase;
import com.example.rakta_seva.data.DonorEntity;
import com.example.rakta_seva.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfileDialog extends DialogFragment {

    public interface OnProfileUpdatedListener {
        void onProfileUpdated();
    }

    private OnProfileUpdatedListener updatedListener;

    public void setOnProfileUpdatedListener(OnProfileUpdatedListener listener) {
        this.updatedListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_edit_profile, null);

        SessionManager session = new SessionManager(requireContext());

        TextInputEditText etName = view.findViewById(R.id.et_edit_name);
        TextInputEditText etEmail = view.findViewById(R.id.et_edit_email);
        TextInputEditText etLocation = view.findViewById(R.id.et_edit_location);
        TextInputEditText etAge = view.findViewById(R.id.et_edit_age);
        Spinner spinnerGender = view.findViewById(R.id.spinner_edit_gender);
        MaterialButton btnSave = view.findViewById(R.id.btn_save_profile);

        // Pre-fill with current data
        etName.setText(session.getUserName());
        etEmail.setText(session.getUserEmail());
        etLocation.setText(session.getUserLocation());
        if (session.getUserAge() > 0) {
            etAge.setText(String.valueOf(session.getUserAge()));
        }

        // Gender spinner
        String[] genders = {"Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, genders);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        // Pre-select gender
        String currentGender = session.getUserGender();
        for (int i = 0; i < genders.length; i++) {
            if (genders[i].equalsIgnoreCase(currentGender)) {
                spinnerGender.setSelection(i);
                break;
            }
        }

        Dialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(view)
                .create();

        btnSave.setOnClickListener(v -> {
            String name = etName.getText() != null ? etName.getText().toString().trim() : "";
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String location = etLocation.getText() != null ? etLocation.getText().toString().trim() : "";
            String ageStr = etAge.getText() != null ? etAge.getText().toString().trim() : "";
            String gender = spinnerGender.getSelectedItem().toString();

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Name is required", Toast.LENGTH_SHORT).show();
                return;
            }

            int age = 0;
            if (!ageStr.isEmpty()) {
                try {
                    age = Integer.parseInt(ageStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Enter a valid age", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Update session
            session.setUserName(name);
            session.setUserEmail(email);
            session.setUserLocation(location);
            session.setUserAge(age);
            session.setUserGender(gender);

            // Update Room DB
            String phone = session.getUserPhone();
            if (!phone.isEmpty()) {
                DonorEntity donor = AppDatabase.getInstance(requireContext())
                        .donorDao().getDonorByPhone(phone);
                if (donor != null) {
                    donor.name = name;
                    donor.email = email;
                    donor.location = location;
                    donor.age = age;
                    donor.gender = gender;
                    AppDatabase.getInstance(requireContext()).donorDao().updateDonor(donor);
                }
            }

            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();

            if (updatedListener != null) {
                updatedListener.onProfileUpdated();
            }

            dialog.dismiss();
        });

        return dialog;
    }
}
