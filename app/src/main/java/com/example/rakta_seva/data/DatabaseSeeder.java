package com.example.rakta_seva.data;

import android.content.Context;
import android.content.SharedPreferences;

public class DatabaseSeeder {

    private static final String PREF_NAME = "rakta_seva_prefs";
    private static final String KEY_DB_SEEDED = "db_seeded";

    public static void seedIfNeeded(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (prefs.getBoolean(KEY_DB_SEEDED, false)) {
            return;
        }

        AppDatabase db = AppDatabase.getInstance(context);

        // Seed 10 donors
        seedDonors(db);

        // Seed 6 emergency requests
        seedRequests(db);

        prefs.edit().putBoolean(KEY_DB_SEEDED, true).apply();
    }

    private static void seedDonors(AppDatabase db) {
        String[][] donorData = {
            {"Aarav Singh", "O-", "25", "Male", "9811111111", "aarav@email.com", "Sector 10 New Delhi", "28.630", "77.209"},
            {"Priya Sharma", "B+", "30", "Female", "9822222222", "priya@email.com", "Connaught Place Delhi", "28.632", "77.219"},
            {"Rahul Verma", "A+", "28", "Male", "9833333333", "rahul@email.com", "Karol Bagh Delhi", "28.645", "77.190"},
            {"Sneha Patel", "AB+", "22", "Female", "9844444444", "sneha@email.com", "Lajpat Nagar Delhi", "28.565", "77.243"},
            {"Kiran Rao", "O+", "35", "Male", "9855555555", "kiran@email.com", "Dwarka Delhi", "28.590", "77.046"},
            {"Meena Iyer", "B-", "27", "Female", "9866666666", "meena@email.com", "Rohini Delhi", "28.730", "77.118"},
            {"Deepak Nair", "A-", "31", "Male", "9877777777", "deepak@email.com", "Vasant Kunj Delhi", "28.520", "77.160"},
            {"Anita Gupta", "O-", "24", "Female", "9888888888", "anita@email.com", "Saket Delhi", "28.527", "77.210"},
            {"Vikram Joshi", "AB-", "29", "Male", "9899999999", "vikram@email.com", "Greater Kailash Delhi", "28.538", "77.232"},
            {"Pooja Menon", "B+", "26", "Female", "9810101010", "pooja@email.com", "Mayur Vihar Delhi", "28.608", "77.293"}
        };

        boolean[] available = {true, true, false, true, true, true, false, true, true, true};
        boolean[] verified = {true, true, true, false, true, true, true, true, false, true};

        for (int i = 0; i < donorData.length; i++) {
            DonorEntity donor = new DonorEntity();
            donor.name = donorData[i][0];
            donor.bloodGroup = donorData[i][1];
            donor.age = Integer.parseInt(donorData[i][2]);
            donor.gender = donorData[i][3];
            donor.phone = donorData[i][4];
            donor.email = donorData[i][5];
            donor.location = donorData[i][6];
            donor.latitude = Double.parseDouble(donorData[i][7]);
            donor.longitude = Double.parseDouble(donorData[i][8]);
            donor.isAvailable = available[i];
            donor.isVerified = verified[i];
            donor.userType = "both";
            donor.lastDonationDate = "";
            db.donorDao().insertDonor(donor);
        }
    }

    private static void seedRequests(AppDatabase db) {
        String[][] requestData = {
            {"Emergency Case", "O-", "Critical", "Apollo Hospital Sector 12", "28.628", "77.215", "2.5", "2", "10 mins ago"},
            {"Accident Victim", "B+", "Urgent", "City Hospital Main Road", "28.635", "77.225", "4.2", "1", "25 mins ago"},
            {"Surgery Patient", "A+", "High Priority", "Medical Center Park Street", "28.620", "77.240", "6.8", "3", "1 hour ago"},
            {"Trauma Case", "AB+", "Critical", "AIIMS Delhi", "28.568", "77.210", "8.1", "2", "45 mins ago"},
            {"Cancer Patient", "O+", "Urgent", "Safdarjung Hospital", "28.573", "77.202", "5.3", "4", "2 hours ago"},
            {"Newborn Emergency", "B-", "High Priority", "RML Hospital", "28.638", "77.197", "3.7", "1", "30 mins ago"}
        };

        for (String[] data : requestData) {
            RequestEntity request = new RequestEntity();
            request.patientName = data[0];
            request.bloodGroup = data[1];
            request.urgency = data[2];
            request.hospitalName = data[3];
            request.latitude = Double.parseDouble(data[4]);
            request.longitude = Double.parseDouble(data[5]);
            request.distanceKm = Double.parseDouble(data[6]);
            request.unitsRequired = Integer.parseInt(data[7]);
            request.timeAgo = data[8];
            request.timestamp = System.currentTimeMillis();
            request.status = "pending";
            request.location = data[3];
            request.acceptedByPhone = "";
            db.requestDao().insertRequest(request);
        }
    }
}
