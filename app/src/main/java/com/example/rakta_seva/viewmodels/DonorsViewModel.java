package com.example.rakta_seva.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rakta_seva.data.AppDatabase;
import com.example.rakta_seva.data.DonorEntity;

import java.util.ArrayList;
import java.util.List;

public class DonorsViewModel extends AndroidViewModel {

    private final AppDatabase db;
    private final MutableLiveData<List<DonorEntity>> allDonors = new MutableLiveData<>();
    private List<DonorEntity> masterList;

    public DonorsViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    public LiveData<List<DonorEntity>> getAllDonors() {
        return allDonors;
    }

    public void loadDonors() {
        masterList = db.donorDao().getAllDonors();
        allDonors.setValue(masterList);
    }

    public void filterDonors(String bloodGroup, boolean availableOnly, String searchQuery) {
        if (masterList == null) {
            masterList = db.donorDao().getAllDonors();
        }

        List<DonorEntity> filtered = new ArrayList<>();
        for (DonorEntity donor : masterList) {
            boolean matchesGroup = bloodGroup.equals("All") || donor.bloodGroup.equals(bloodGroup);
            boolean matchesAvailable = !availableOnly || donor.isAvailable;
            boolean matchesSearch = searchQuery == null || searchQuery.isEmpty()
                    || donor.name.toLowerCase().contains(searchQuery.toLowerCase())
                    || donor.bloodGroup.toLowerCase().contains(searchQuery.toLowerCase());

            if (matchesGroup && matchesAvailable && matchesSearch) {
                filtered.add(donor);
            }
        }
        allDonors.setValue(filtered);
    }
}
