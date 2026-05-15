package com.example.rakta_seva.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.rakta_seva.R;
import com.example.rakta_seva.data.AppDatabase;
import com.example.rakta_seva.data.DonorEntity;
import com.example.rakta_seva.data.RequestEntity;
import com.example.rakta_seva.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Random;

public class DonorDetailDialog extends DialogFragment {

    private static final String ARG_DONOR_ID = "donor_id";
    private static final String ARG_DONOR_PHONE = "donor_phone";

    public static DonorDetailDialog newInstance(String donorPhone) {
        DonorDetailDialog dialog = new DonorDetailDialog();
        Bundle args = new Bundle();
        args.putString(ARG_DONOR_PHONE, donorPhone);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_donor_detail, null);

        String donorPhone = getArguments() != null ? getArguments().getString(ARG_DONOR_PHONE, "") : "";
        AppDatabase db = AppDatabase.getInstance(requireContext());
        DonorEntity donor = db.donorDao().getDonorByPhone(donorPhone);

        if (donor == null) {
            return new MaterialAlertDialogBuilder(requireContext())
                    .setMessage("Donor not found")
                    .setPositiveButton("OK", null)
                    .create();
        }

        TextView tvAvatar = view.findViewById(R.id.tv_detail_avatar);
        TextView tvName = view.findViewById(R.id.tv_detail_name);
        TextView tvBlood = view.findViewById(R.id.tv_detail_blood);
        TextView tvVerified = view.findViewById(R.id.tv_detail_verified);
        TextView tvEmail = view.findViewById(R.id.tv_detail_email);
        TextView tvPhone = view.findViewById(R.id.tv_detail_phone);
        LinearLayout llPhoneRow = view.findViewById(R.id.ll_phone_row);
        TextView tvLocation = view.findViewById(R.id.tv_detail_location);
        TextView tvLastDonation = view.findViewById(R.id.tv_detail_last_donation);
        MaterialButton btnRequest = view.findViewById(R.id.btn_request_donor);

        // Set avatar
        String initial = donor.name.isEmpty() ? "?" : String.valueOf(donor.name.charAt(0)).toUpperCase();
        tvAvatar.setText(initial);

        tvName.setText(donor.name);
        tvBlood.setText(donor.bloodGroup);

        // Verified
        if (donor.isVerified) {
            tvVerified.setVisibility(View.VISIBLE);
        } else {
            tvVerified.setVisibility(View.GONE);
        }

        tvEmail.setText(donor.email);

        // Only show phone if donor is available
        if (donor.isAvailable) {
            llPhoneRow.setVisibility(View.VISIBLE);
            tvPhone.setText("+91 " + donor.phone);
        } else {
            llPhoneRow.setVisibility(View.GONE);
        }

        tvLocation.setText(donor.location);

        String lastDonation = donor.lastDonationDate;
        tvLastDonation.setText(lastDonation == null || lastDonation.isEmpty() ? "No donations yet" : lastDonation);

        Dialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(view)
                .create();

        btnRequest.setOnClickListener(v -> {
            // Create request pre-filled with donor's blood group
            RequestEntity request = new RequestEntity();
            request.bloodGroup = donor.bloodGroup;
            request.urgency = "Urgent";
            request.hospitalName = "Requested via app";
            request.location = donor.location;
            request.unitsRequired = 1;
            request.patientName = "Direct Request";
            request.timestamp = System.currentTimeMillis();
            request.timeAgo = "Just now";
            request.distanceKm = Math.round((1.0 + new Random().nextDouble() * 8.9) * 10.0) / 10.0;
            request.status = "pending";
            request.acceptedByPhone = "";
            request.latitude = donor.latitude;
            request.longitude = donor.longitude;

            db.requestDao().insertRequest(request);

            Toast.makeText(requireContext(),
                    "Request sent to " + donor.name, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        return dialog;
    }
}
