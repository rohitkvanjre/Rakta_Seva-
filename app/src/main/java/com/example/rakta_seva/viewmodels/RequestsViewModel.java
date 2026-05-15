package com.example.rakta_seva.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rakta_seva.data.AppDatabase;
import com.example.rakta_seva.data.RequestEntity;

import java.util.ArrayList;
import java.util.List;

public class RequestsViewModel extends AndroidViewModel {

    private final AppDatabase db;
    private final MutableLiveData<List<RequestEntity>> nearbyRequests = new MutableLiveData<>();
    private final MutableLiveData<List<RequestEntity>> myRequests = new MutableLiveData<>();

    public RequestsViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    public LiveData<List<RequestEntity>> getNearbyRequests() {
        return nearbyRequests;
    }

    public LiveData<List<RequestEntity>> getMyRequests() {
        return myRequests;
    }

    public void loadNearbyRequests() {
        List<RequestEntity> requests = db.requestDao().getPendingRequestsByDistance();
        nearbyRequests.setValue(requests);
    }

    public void loadMyRequests(String phone) {
        List<RequestEntity> allRequests = db.requestDao().getAllRequests();
        List<RequestEntity> myList = new ArrayList<>();
        for (RequestEntity request : allRequests) {
            if (phone.equals(request.acceptedByPhone) || !"pending".equals(request.status)) {
                myList.add(request);
            }
        }
        myRequests.setValue(myList);
    }

    public void acceptRequest(int id, String phone) {
        db.requestDao().updateStatus(id, "accepted", phone);
        loadNearbyRequests();
    }

    public void rejectRequest(int id) {
        db.requestDao().updateStatus(id, "rejected", "");
        loadNearbyRequests();
    }

    public void postNewRequest(RequestEntity entity) {
        db.requestDao().insertRequest(entity);
        loadNearbyRequests();
    }
}
