package com.example.rakta_seva.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "requests")
public class RequestEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String patientName;
    public String bloodGroup;
    public String urgency; // "Critical", "Urgent", "High Priority"
    public String hospitalName;
    public String location;
    public double latitude;
    public double longitude;
    public double distanceKm;
    public int unitsRequired;
    public String timeAgo;
    public long timestamp;
    public String status; // "pending", "accepted", "rejected"
    public String acceptedByPhone;
}
