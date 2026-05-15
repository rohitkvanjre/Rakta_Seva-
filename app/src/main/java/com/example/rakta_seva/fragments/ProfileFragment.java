package com.example.rakta_seva.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rakta_seva.LandingActivity;
import com.example.rakta_seva.R;
import com.example.rakta_seva.data.AppDatabase;
import com.example.rakta_seva.dialogs.EditProfileDialog;
import com.example.rakta_seva.utils.SessionManager;
import com.example.rakta_seva.utils.TimeAgoFormatter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ProfileFragment extends Fragment {

    private SessionManager session;
    private TextView tvAvatar, tvName, tvBlood, tvVerified, tvEmail, tvPhone;
    private TextView tvLocation, tvAgeGender, tvLastDonation, tvDonationTimeAgo;
    private SwitchMaterial switchAvailability;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        session = new SessionManager(requireContext());

        // Bind views
        tvAvatar = view.findViewById(R.id.tv_avatar);
        tvName = view.findViewById(R.id.tv_profile_name);
        tvBlood = view.findViewById(R.id.tv_profile_blood);
        tvVerified = view.findViewById(R.id.tv_profile_verified);
        tvEmail = view.findViewById(R.id.tv_profile_email);
        tvPhone = view.findViewById(R.id.tv_profile_phone);
        tvLocation = view.findViewById(R.id.tv_profile_location);
        tvAgeGender = view.findViewById(R.id.tv_profile_age_gender);
        tvLastDonation = view.findViewById(R.id.tv_profile_last_donation);
        tvDonationTimeAgo = view.findViewById(R.id.tv_donation_time_ago);
        switchAvailability = view.findViewById(R.id.switch_profile_availability);
        ImageView ivEditProfile = view.findViewById(R.id.iv_edit_profile);

        // Populate data
        populateProfile();

        // Availability toggle (synced with home)
        switchAvailability.setChecked(session.isAvailable());
        switchAvailability.setOnCheckedChangeListener((buttonView, isChecked) -> {
            session.setAvailable(isChecked);
            String phone = session.getUserPhone();
            if (!phone.isEmpty()) {
                AppDatabase.getInstance(requireContext()).donorDao()
                        .updateAvailability(phone, isChecked);
            }
            Toast.makeText(requireContext(),
                    isChecked ? "You are now available for donation" : "You are now unavailable",
                    Toast.LENGTH_SHORT).show();
        });

        // Edit profile
        ivEditProfile.setOnClickListener(v -> {
            EditProfileDialog dialog = new EditProfileDialog();
            dialog.setOnProfileUpdatedListener(this::populateProfile);
            dialog.show(getChildFragmentManager(), "EditProfileDialog");
        });

        // Change Blood Group
        view.findViewById(R.id.card_change_blood_group).setOnClickListener(v -> {
            showChangeBloodGroupDialog();
        });

        // Logout
        view.findViewById(R.id.card_logout).setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        session.clearSession();
                        Intent intent = new Intent(requireContext(), LandingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        return view;
    }

    private void populateProfile() {
        String name = session.getUserName();
        tvAvatar.setText(name.isEmpty() ? "?" : String.valueOf(name.charAt(0)).toUpperCase());
        tvName.setText(name);
        tvBlood.setText(session.getUserBloodGroup());
        tvEmail.setText(session.getUserEmail());
        tvPhone.setText("+91 " + session.getUserPhone());
        tvLocation.setText(session.getUserLocation());

        int age = session.getUserAge();
        String gender = session.getUserGender();
        tvAgeGender.setText(age + " years • " + gender);

        String lastDonation = session.getLastDonation();
        if (lastDonation != null && !lastDonation.isEmpty()) {
            tvLastDonation.setText(lastDonation);
            String timeAgo = TimeAgoFormatter.monthsAgoFromDate(lastDonation);
            tvDonationTimeAgo.setText(timeAgo);
        } else {
            tvLastDonation.setText("No donations yet");
            tvDonationTimeAgo.setText("");
        }

        if (session.isVerified()) {
            tvVerified.setVisibility(View.VISIBLE);
        } else {
            tvVerified.setVisibility(View.GONE);
        }
    }

    private void showChangeBloodGroupDialog() {
        String[] bloodGroups = {"O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"};
        String current = session.getUserBloodGroup();

        int selectedIndex = 0;
        for (int i = 0; i < bloodGroups.length; i++) {
            if (bloodGroups[i].equals(current)) {
                selectedIndex = i;
                break;
            }
        }

        final int[] chosen = {selectedIndex};

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Change Blood Group")
                .setSingleChoiceItems(bloodGroups, selectedIndex, (dialog, which) -> {
                    chosen[0] = which;
                })
                .setPositiveButton("Save", (dialog, which) -> {
                    String newBloodGroup = bloodGroups[chosen[0]];
                    session.setUserBloodGroup(newBloodGroup);

                    // Update Room DB
                    String phone = session.getUserPhone();
                    if (!phone.isEmpty()) {
                        com.example.rakta_seva.data.DonorEntity donor =
                                AppDatabase.getInstance(requireContext()).donorDao().getDonorByPhone(phone);
                        if (donor != null) {
                            donor.bloodGroup = newBloodGroup;
                            AppDatabase.getInstance(requireContext()).donorDao().updateDonor(donor);
                        }
                    }

                    tvBlood.setText(newBloodGroup);
                    Toast.makeText(requireContext(),
                            "Blood group changed to " + newBloodGroup, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
