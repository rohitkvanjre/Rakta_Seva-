package com.example.rakta_seva.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rakta_seva.data.AppDatabase;
import com.example.rakta_seva.data.RequestEntity;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final AppDatabase db;
    private final MutableLiveData<List<RequestEntity>> pendingRequests = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isAvailable = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    public LiveData<List<RequestEntity>> getPendingRequests() {
        return pendingRequests;
    }

    public LiveData<Boolean> getIsAvailable() {
        return isAvailable;
    }

    public void loadTopRequests() {
        List<RequestEntity> requests = db.requestDao().getTopPendingRequests(3);
        pendingRequests.setValue(requests);
    }

    public void setAvailability(boolean available) {
        isAvailable.setValue(available);
    }

    public void acceptRequest(int id, String phone) {
        db.requestDao().updateStatus(id, "accepted", phone);
        loadTopRequests();
    }

    public void rejectRequest(int id) {
        db.requestDao().updateStatus(id, "rejected", "");
        loadTopRequests();
    }

    public void postNewRequest(RequestEntity entity) {
        db.requestDao().insertRequest(entity);
        loadTopRequests();
    }
}
