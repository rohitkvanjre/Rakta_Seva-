package com.example.rakta_seva.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rakta_seva.R;
import com.example.rakta_seva.adapters.DonorAdapter;
import com.example.rakta_seva.data.DonorEntity;
import com.example.rakta_seva.dialogs.DonorDetailDialog;
import com.example.rakta_seva.viewmodels.DonorsViewModel;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class DonorsFragment extends Fragment {

    private RecyclerView rvDonors;
    private DonorAdapter adapter;
    private LinearLayout filterContainer;
    private LinearLayout emptyState;
    private String selectedFilter = "All";
    private boolean availableOnly = false;
    private String searchQuery = "";
    private DonorsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donors, container, false);

        viewModel = new ViewModelProvider(this).get(DonorsViewModel.class);

        rvDonors = view.findViewById(R.id.rv_donors);
        filterContainer = view.findViewById(R.id.filter_container);
        emptyState = view.findViewById(R.id.ll_empty_state);
        EditText etSearch = view.findViewById(R.id.et_search);

        // Setup adapter
        adapter = new DonorAdapter(requireContext(), new ArrayList<>());
        adapter.setOnDonorClickListener(donor -> {
            DonorDetailDialog dialog = DonorDetailDialog.newInstance(donor.phone);
            dialog.show(getChildFragmentManager(), "DonorDetailDialog");
        });

        rvDonors.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvDonors.setAdapter(adapter);

        // Observe donors
        viewModel.getAllDonors().observe(getViewLifecycleOwner(), donors -> {
            if (donors != null) {
                adapter.updateDonors(donors);
                if (donors.isEmpty()) {
                    emptyState.setVisibility(View.VISIBLE);
                    rvDonors.setVisibility(View.GONE);
                } else {
                    emptyState.setVisibility(View.GONE);
                    rvDonors.setVisibility(View.VISIBLE);
                }
            }
        });

        // Setup search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString();
                viewModel.filterDonors(selectedFilter, availableOnly, searchQuery);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        setupFilters();

        // Initial load
        viewModel.loadDonors();

        return view;
    }

    private void setupFilters() {
        String[] filters = {"All", "O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"};

        for (String filter : filters) {
            Chip chip = new Chip(requireContext());
            chip.setText(filter);
            chip.setCheckable(true);
            chip.setChecked(filter.equals("All"));

            if (filter.equals("All")) {
                chip.setChipBackgroundColorResource(R.color.red_primary);
                chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            } else {
                chip.setChipBackgroundColorResource(R.color.white);
                chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary));
            }
            chip.setChipStrokeColorResource(R.color.gray_medium);
            chip.setChipStrokeWidth(1f);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMarginEnd(8);
            chip.setLayoutParams(params);

            chip.setOnClickListener(v -> {
                selectedFilter = filter;
                // Update all chip styles
                for (int i = 0; i < filterContainer.getChildCount(); i++) {
                    View child = filterContainer.getChildAt(i);
                    if (child instanceof Chip) {
                        Chip c = (Chip) child;
                        c.setChecked(false);
                        c.setChipBackgroundColorResource(R.color.white);
                        c.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary));
                    }
                }
                chip.setChecked(true);
                chip.setChipBackgroundColorResource(R.color.red_primary);
                chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));

                viewModel.filterDonors(selectedFilter, availableOnly, searchQuery);
            });

            filterContainer.addView(chip);
        }

        // Add "Available Only" toggle chip
        Chip availableChip = new Chip(requireContext());
        availableChip.setText("Available Only");
        availableChip.setCheckable(true);
        availableChip.setChecked(false);
        availableChip.setChipBackgroundColorResource(R.color.white);
        availableChip.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary));
        availableChip.setChipStrokeColorResource(R.color.green_available);
        availableChip.setChipStrokeWidth(1f);
        availableChip.setChipIconResource(R.drawable.ic_verified);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMarginEnd(8);
        availableChip.setLayoutParams(params);

        availableChip.setOnClickListener(v -> {
            availableOnly = availableChip.isChecked();
            if (availableOnly) {
                availableChip.setChipBackgroundColorResource(R.color.green_light);
                availableChip.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_available));
            } else {
                availableChip.setChipBackgroundColorResource(R.color.white);
                availableChip.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary));
            }
            viewModel.filterDonors(selectedFilter, availableOnly, searchQuery);
        });

        filterContainer.addView(availableChip);
    }
}
