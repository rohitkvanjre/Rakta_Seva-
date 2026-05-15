package com.example.rakta_seva.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rakta_seva.R;
import com.example.rakta_seva.data.RequestEntity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private List<RequestEntity> requests;
    private final Context context;
    private OnRequestActionListener listener;
    private boolean showStatusMode = false; // true for "My Requests" tab

    public interface OnRequestActionListener {
        void onAccept(RequestEntity request, int position);
        void onReject(RequestEntity request, int position);
    }

    public RequestAdapter(Context context, List<RequestEntity> requests) {
        this.context = context;
        this.requests = new ArrayList<>(requests);
    }

    public void setShowStatusMode(boolean statusMode) {
        this.showStatusMode = statusMode;
    }

    public void setOnRequestActionListener(OnRequestActionListener listener) {
        this.listener = listener;
    }

    public void updateRequests(List<RequestEntity> newRequests) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() { return requests.size(); }
            @Override
            public int getNewListSize() { return newRequests.size(); }
            @Override
            public boolean areItemsTheSame(int oldPos, int newPos) {
                return requests.get(oldPos).id == newRequests.get(newPos).id;
            }
            @Override
            public boolean areContentsTheSame(int oldPos, int newPos) {
                RequestEntity old = requests.get(oldPos);
                RequestEntity cur = newRequests.get(newPos);
                return old.id == cur.id && old.status.equals(cur.status);
            }
        });
        this.requests = new ArrayList<>(newRequests);
        result.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request_card, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        RequestEntity request = requests.get(position);

        holder.tvBloodGroup.setText(request.bloodGroup);
        holder.tvPatientName.setText(request.patientName);
        holder.tvUrgency.setText(request.urgency);
        holder.tvTimeAgo.setText(request.timeAgo);
        holder.tvDistance.setText(String.format("%.1f km", request.distanceKm));
        holder.tvHospital.setText(request.hospitalName);
        holder.tvUnits.setText("Required: " + request.unitsRequired + " units");

        // Set urgency background tint
        int urgencyColor;
        if ("Critical".equals(request.urgency)) {
            urgencyColor = ContextCompat.getColor(context, R.color.urgency_critical);
        } else if ("Urgent".equals(request.urgency)) {
            urgencyColor = ContextCompat.getColor(context, R.color.urgency_urgent);
        } else {
            urgencyColor = ContextCompat.getColor(context, R.color.urgency_high);
        }
        holder.tvUrgency.setTextColor(urgencyColor);

        if (showStatusMode) {
            // Status mode: show status badge, hide action buttons
            holder.llActionButtons.setVisibility(View.GONE);
            holder.tvStatusBadge.setVisibility(View.VISIBLE);

            if ("accepted".equals(request.status)) {
                holder.tvStatusBadge.setText("Accepted");
                holder.tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.status_accepted));
                holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_verified_tag);
            } else if ("rejected".equals(request.status)) {
                holder.tvStatusBadge.setText("Rejected");
                holder.tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.status_rejected));
                holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_status_badge);
            } else {
                holder.tvStatusBadge.setText("Pending");
                holder.tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.status_pending));
                holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_status_badge);
            }
        } else {
            // Action mode: show buttons, hide status badge
            holder.tvStatusBadge.setVisibility(View.GONE);

            if ("pending".equals(request.status)) {
                holder.llActionButtons.setVisibility(View.VISIBLE);
                holder.btnAccept.setText("Accept");
                holder.btnAccept.setEnabled(true);
                holder.btnReject.setEnabled(true);
            } else {
                holder.llActionButtons.setVisibility(View.GONE);
            }
        }

        holder.btnAccept.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAccept(request, holder.getAdapterPosition());
            }
        });

        holder.btnReject.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReject(request, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvBloodGroup, tvPatientName, tvUrgency, tvTimeAgo;
        TextView tvDistance, tvHospital, tvUnits, tvStatusBadge;
        MaterialButton btnAccept, btnReject;
        LinearLayout llActionButtons;

        RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBloodGroup = itemView.findViewById(R.id.tv_blood_group);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvUrgency = itemView.findViewById(R.id.tv_urgency);
            tvTimeAgo = itemView.findViewById(R.id.tv_time_ago);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            tvHospital = itemView.findViewById(R.id.tv_hospital);
            tvUnits = itemView.findViewById(R.id.tv_units);
            tvStatusBadge = itemView.findViewById(R.id.tv_status_badge);
            btnAccept = itemView.findViewById(R.id.btn_accept);
            btnReject = itemView.findViewById(R.id.btn_reject);
            llActionButtons = itemView.findViewById(R.id.ll_action_buttons);
        }
    }
}
