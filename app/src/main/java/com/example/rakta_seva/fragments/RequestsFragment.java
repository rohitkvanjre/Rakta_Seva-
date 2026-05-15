package com.example.rakta_seva.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rakta_seva.R;
import com.example.rakta_seva.adapters.RequestAdapter;
import com.example.rakta_seva.data.RequestEntity;
import com.example.rakta_seva.dialogs.RequestBloodDialog;
import com.example.rakta_seva.utils.SessionManager;
import com.example.rakta_seva.viewmodels.RequestsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RequestsFragment extends Fragment {

    private RecyclerView rvNearbyRequests;
    private RecyclerView rvMyRequests;
    private LinearLayout emptyState;
    private TextView tvEmptyMessage;
    private RequestAdapter nearbyAdapter;
    private RequestAdapter myAdapter;
    private RequestsViewModel viewModel;
    private SessionManager sessionManager;
    private int currentTab = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        sessionManager = new SessionManager(requireContext());
        viewModel = new ViewModelProvider(this).get(RequestsViewModel.class);

        rvNearbyRequests = view.findViewById(R.id.rv_nearby_requests);
        rvMyRequests = view.findViewById(R.id.rv_my_requests);
        emptyState = view.findViewById(R.id.ll_empty_state);
        tvEmptyMessage = view.findViewById(R.id.tv_empty_message);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        FloatingActionButton fab = view.findViewById(R.id.fab_add_request);

        // Setup Nearby Requests adapter
        nearbyAdapter = new RequestAdapter(requireContext(), new ArrayList<>());
        nearbyAdapter.setOnRequestActionListener(new RequestAdapter.OnRequestActionListener() {
            @Override
            public void onAccept(RequestEntity request, int position) {
                String phone = sessionManager.getUserPhone();
                viewModel.acceptRequest(request.id, phone);
                viewModel.loadMyRequests(phone);
                Toast.makeText(requireContext(),
                        "Request accepted! The hospital will contact you.",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onReject(RequestEntity request, int position) {
                viewModel.rejectRequest(request.id);
                String phone = sessionManager.getUserPhone();
                viewModel.loadMyRequests(phone);
                Toast.makeText(requireContext(),
                        "Request rejected.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        rvNearbyRequests.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvNearbyRequests.setAdapter(nearbyAdapter);

        // Setup My Requests adapter (status mode)
        myAdapter = new RequestAdapter(requireContext(), new ArrayList<>());
        myAdapter.setShowStatusMode(true);
        rvMyRequests.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvMyRequests.setAdapter(myAdapter);

        // Observe nearby requests
        viewModel.getNearbyRequests().observe(getViewLifecycleOwner(), requests -> {
            if (requests != null && currentTab == 0) {
                nearbyAdapter.updateRequests(requests);
                updateEmptyState(requests.isEmpty(), "No nearby requests found");
            }
        });

        // Observe my requests
        viewModel.getMyRequests().observe(getViewLifecycleOwner(), requests -> {
            if (requests != null && currentTab == 1) {
                myAdapter.updateRequests(requests);
                updateEmptyState(requests.isEmpty(), "You haven\'t responded to any requests yet");
            }
        });

        // Tab switching
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                if (currentTab == 0) {
                    rvNearbyRequests.setVisibility(View.VISIBLE);
                    rvMyRequests.setVisibility(View.GONE);
                    viewModel.loadNearbyRequests();
                } else {
                    rvNearbyRequests.setVisibility(View.GONE);
                    rvMyRequests.setVisibility(View.VISIBLE);
                    viewModel.loadMyRequests(sessionManager.getUserPhone());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // FAB
        fab.setOnClickListener(v -> {
            RequestBloodDialog dialog = new RequestBloodDialog();
            dialog.setOnRequestPostedListener(() -> {
                viewModel.loadNearbyRequests();
                viewModel.loadMyRequests(sessionManager.getUserPhone());
            });
            dialog.show(getChildFragmentManager(), "RequestBloodDialog");
        });

        // Initial load
        viewModel.loadNearbyRequests();
        viewModel.loadMyRequests(sessionManager.getUserPhone());

        return view;
    }

    private void updateEmptyState(boolean isEmpty, String message) {
        if (isEmpty) {
            emptyState.setVisibility(View.VISIBLE);
            tvEmptyMessage.setText(message);
            if (currentTab == 0) {
                rvNearbyRequests.setVisibility(View.GONE);
            } else {
                rvMyRequests.setVisibility(View.GONE);
            }
        } else {
            emptyState.setVisibility(View.GONE);
            if (currentTab == 0) {
                rvNearbyRequests.setVisibility(View.VISIBLE);
            } else {
                rvMyRequests.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadNearbyRequests();
        viewModel.loadMyRequests(sessionManager.getUserPhone());
    }
}
