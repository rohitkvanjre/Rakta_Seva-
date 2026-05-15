package com.example.rakta_seva.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rakta_seva.MainActivity;
import com.example.rakta_seva.R;
import com.example.rakta_seva.adapters.RequestAdapter;
import com.example.rakta_seva.data.AppDatabase;
import com.example.rakta_seva.data.RequestEntity;
import com.example.rakta_seva.dialogs.RequestBloodDialog;
import com.example.rakta_seva.utils.SessionManager;
import com.example.rakta_seva.viewmodels.HomeViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvRequests;
    private SwitchMaterial switchAvailability;
    private TextView tvLastDonation, tvAvailabilityText;
    private SessionManager sessionManager;
    private HomeViewModel viewModel;
    private RequestAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sessionManager = new SessionManager(requireContext());
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        rvRequests = view.findViewById(R.id.rv_emergency_requests);
        switchAvailability = view.findViewById(R.id.switch_availability);
        tvLastDonation = view.findViewById(R.id.tv_last_donation);
        tvAvailabilityText = view.findViewById(R.id.tv_availability_text);
        TextView tvViewAll = view.findViewById(R.id.tv_view_all);
        MaterialButton btnRequestBlood = view.findViewById(R.id.btn_request_blood);

        // Set up availability toggle
        boolean isAvailable = sessionManager.isAvailable();
        switchAvailability.setChecked(isAvailable);
        updateAvailabilityText(isAvailable);

        String lastDonation = sessionManager.getLastDonation();
        if (!lastDonation.isEmpty()) {
            tvLastDonation.setText("Last donation: " + lastDonation);
        }

        switchAvailability.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sessionManager.setAvailable(isChecked);
            String phone = sessionManager.getUserPhone();
            if (!phone.isEmpty()) {
                AppDatabase.getInstance(requireContext()).donorDao()
                        .updateAvailability(phone, isChecked);
            }
            updateAvailabilityText(isChecked);
            Toast.makeText(requireContext(),
                    isChecked ? "You are now available for donation" : "You are now unavailable",
                    Toast.LENGTH_SHORT).show();
        });

        // Setup RecyclerView
        adapter = new RequestAdapter(requireContext(), new ArrayList<>());
        adapter.setOnRequestActionListener(new RequestAdapter.OnRequestActionListener() {
            @Override
            public void onAccept(RequestEntity request, int position) {
                String phone = sessionManager.getUserPhone();
                viewModel.acceptRequest(request.id, phone);
                Toast.makeText(requireContext(),
                        "Request accepted! Thank you for saving a life.",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onReject(RequestEntity request, int position) {
                viewModel.rejectRequest(request.id);
                Toast.makeText(requireContext(),
                        "Request rejected.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        rvRequests.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvRequests.setAdapter(adapter);

        // Observe pending requests
        viewModel.getPendingRequests().observe(getViewLifecycleOwner(), requests -> {
            if (requests != null) {
                adapter.updateRequests(requests);
            }
        });

        // Load top 3 requests
        viewModel.loadTopRequests();

        // View All click
        tvViewAll.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).selectTab(R.id.nav_requests);
            }
        });

        // Request Blood button → opens dialog
        btnRequestBlood.setOnClickListener(v -> {
            RequestBloodDialog dialog = new RequestBloodDialog();
            dialog.setOnRequestPostedListener(() -> viewModel.loadTopRequests());
            dialog.show(getChildFragmentManager(), "RequestBloodDialog");
        });

        // Quick action cards
        view.findViewById(R.id.card_view_donors).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).selectTab(R.id.nav_donors);
            }
        });

        view.findViewById(R.id.card_my_requests).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).selectTab(R.id.nav_requests);
            }
        });

        return view;
    }

    private void updateAvailabilityText(boolean isAvailable) {
        if (tvAvailabilityText != null) {
            tvAvailabilityText.setText(isAvailable
                    ? "You are Available for\nDonation"
                    : "You are Unavailable for\nDonation");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadTopRequests();
    }
}
