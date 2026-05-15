package com.example.rakta_seva.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "donors")
public class DonorEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String bloodGroup;
    public int age;
    public String gender;
    public String phone;
    public String email;
    public String location;
    public double latitude;
    public double longitude;
    public String lastDonationDate;
    public boolean isAvailable;
    public boolean isVerified;
    public String userType; // "donor", "requester", "both"
}
