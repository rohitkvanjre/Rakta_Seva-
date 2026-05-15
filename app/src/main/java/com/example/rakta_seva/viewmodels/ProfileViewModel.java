package com.example.rakta_seva.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.rakta_seva.data.AppDatabase;
import com.example.rakta_seva.data.DonorEntity;
import com.example.rakta_seva.utils.SessionManager;

public class ProfileViewModel extends AndroidViewModel {

    private final SessionManager sessionManager;
    private final AppDatabase db;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        sessionManager = new SessionManager(application);
        db = AppDatabase.getInstance(application);
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void updateAvailability(boolean available) {
        sessionManager.setAvailable(available);
        String phone = sessionManager.getUserPhone();
        if (!phone.isEmpty()) {
            db.donorDao().updateAvailability(phone, available);
        }
    }

    public void updateProfile(String name, String email, String location, int age, String gender) {
        sessionManager.setUserName(name);
        sessionManager.setUserEmail(email);
        sessionManager.setUserLocation(location);
        sessionManager.setUserAge(age);
        sessionManager.setUserGender(gender);

        // Update Room DB as well
        String phone = sessionManager.getUserPhone();
        if (!phone.isEmpty()) {
            DonorEntity donor = db.donorDao().getDonorByPhone(phone);
            if (donor != null) {
                donor.name = name;
                donor.email = email;
                donor.location = location;
                donor.age = age;
                donor.gender = gender;
                db.donorDao().updateDonor(donor);
            }
        }
    }

    public void updateBloodGroup(String bloodGroup) {
        sessionManager.setUserBloodGroup(bloodGroup);
        String phone = sessionManager.getUserPhone();
        if (!phone.isEmpty()) {
            DonorEntity donor = db.donorDao().getDonorByPhone(phone);
            if (donor != null) {
                donor.bloodGroup = bloodGroup;
                db.donorDao().updateDonor(donor);
            }
        }
    }

    public void logout() {
        sessionManager.clearSession();
    }
}
