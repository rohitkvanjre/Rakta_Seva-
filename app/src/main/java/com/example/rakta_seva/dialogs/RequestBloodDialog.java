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
import com.example.rakta_seva.data.RequestEntity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

public class RequestBloodDialog extends DialogFragment {

    public interface OnRequestPostedListener {
        void onRequestPosted();
    }

    private OnRequestPostedListener postedListener;

    public void setOnRequestPostedListener(OnRequestPostedListener listener) {
        this.postedListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_request_blood, null);

        Spinner spinnerBloodGroup = view.findViewById(R.id.spinner_blood_group);
        Spinner spinnerUrgency = view.findViewById(R.id.spinner_urgency);
        TextInputEditText etHospital = view.findViewById(R.id.et_hospital);
        TextInputEditText etLocation = view.findViewById(R.id.et_location);
        TextInputEditText etUnits = view.findViewById(R.id.et_units);
        MaterialButton btnSubmit = view.findViewById(R.id.btn_submit);

        // Setup blood group spinner
        String[] bloodGroups = {"O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"};
        ArrayAdapter<String> bgAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, bloodGroups);
        bgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodGroup.setAdapter(bgAdapter);

        // Setup urgency spinner
        String[] urgencies = {"Critical", "Urgent", "High Priority"};
        ArrayAdapter<String> urgAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, urgencies);
        urgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUrgency.setAdapter(urgAdapter);

        Dialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(view)
                .create();

        btnSubmit.setOnClickListener(v -> {
            String hospital = etHospital.getText() != null ? etHospital.getText().toString().trim() : "";
            String location = etLocation.getText() != null ? etLocation.getText().toString().trim() : "";
            String unitsStr = etUnits.getText() != null ? etUnits.getText().toString().trim() : "";

            if (hospital.isEmpty() || location.isEmpty() || unitsStr.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int units;
            try {
                units = Integer.parseInt(unitsStr);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Enter a valid number for units", Toast.LENGTH_SHORT).show();
                return;
            }

            RequestEntity request = new RequestEntity();
            request.bloodGroup = spinnerBloodGroup.getSelectedItem().toString();
            request.urgency = spinnerUrgency.getSelectedItem().toString();
            request.hospitalName = hospital;
            request.location = location;
            request.unitsRequired = units;
            request.patientName = "Emergency Request";
            request.timestamp = System.currentTimeMillis();
            request.timeAgo = "Just now";
            request.distanceKm = Math.round((1.0 + new Random().nextDouble() * 8.9) * 10.0) / 10.0;
            request.status = "pending";
            request.acceptedByPhone = "";
            request.latitude = 28.630 + (new Random().nextDouble() - 0.5) * 0.1;
            request.longitude = 77.215 + (new Random().nextDouble() - 0.5) * 0.1;

            AppDatabase.getInstance(requireContext()).requestDao().insertRequest(request);

            Toast.makeText(requireContext(), "Request posted successfully", Toast.LENGTH_SHORT).show();

            if (postedListener != null) {
                postedListener.onRequestPosted();
            }

            dialog.dismiss();
        });

        return dialog;
    }
}
