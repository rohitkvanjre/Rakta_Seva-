package com.example.rakta_seva.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rakta_seva.R;
import com.example.rakta_seva.data.DonorEntity;

import java.util.ArrayList;
import java.util.List;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.DonorViewHolder> {

    private List<DonorEntity> donors;
    private final Context context;
    private OnDonorClickListener listener;

    public interface OnDonorClickListener {
        void onDonorClick(DonorEntity donor);
    }

    public DonorAdapter(Context context, List<DonorEntity> donors) {
        this.context = context;
        this.donors = new ArrayList<>(donors);
    }

    public void setOnDonorClickListener(OnDonorClickListener listener) {
        this.listener = listener;
    }

    public void updateDonors(List<DonorEntity> newDonors) {
        this.donors = new ArrayList<>(newDonors);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_donor_card, parent, false);
        return new DonorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonorViewHolder holder, int position) {
        DonorEntity donor = donors.get(position);

        // Set initials avatar
        String initial = donor.name.isEmpty() ? "?" : String.valueOf(donor.name.charAt(0)).toUpperCase();
        holder.tvBloodGroup.setText(initial);

        holder.tvDonorName.setText(donor.name);
        holder.tvDonorLocation.setText(donor.location);
        holder.tvDonorDetails.setText(donor.age + " yrs • " + donor.gender);

        // Blood group badge
        holder.tvDonorBloodBadge.setText(donor.bloodGroup);
        holder.tvDonorBloodBadge.setVisibility(View.VISIBLE);

        // Verified icon
        holder.ivVerified.setVisibility(donor.isVerified ? View.VISIBLE : View.GONE);

        // Availability
        if (donor.isAvailable) {
            holder.tvAvailability.setText("Available");
            holder.tvAvailability.setTextColor(context.getResources().getColor(R.color.green_available));
            holder.tvAvailability.setBackgroundResource(R.drawable.bg_verified_tag);
        } else {
            holder.tvAvailability.setText("Unavailable");
            holder.tvAvailability.setTextColor(context.getResources().getColor(R.color.gray_medium));
            holder.tvAvailability.setBackgroundResource(R.drawable.bg_urgency_tag);
        }

        // Click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDonorClick(donor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return donors.size();
    }

    static class DonorViewHolder extends RecyclerView.ViewHolder {
        TextView tvBloodGroup, tvDonorName, tvDonorLocation, tvDonorDetails;
        TextView tvAvailability, tvDonorBloodBadge;
        ImageView ivVerified;

        DonorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBloodGroup = itemView.findViewById(R.id.tv_blood_group);
            tvDonorName = itemView.findViewById(R.id.tv_donor_name);
            tvDonorLocation = itemView.findViewById(R.id.tv_donor_location);
            tvDonorDetails = itemView.findViewById(R.id.tv_donor_details);
            tvAvailability = itemView.findViewById(R.id.tv_availability);
            tvDonorBloodBadge = itemView.findViewById(R.id.tv_donor_blood_badge);
            ivVerified = itemView.findViewById(R.id.iv_verified);
        }
    }
}
