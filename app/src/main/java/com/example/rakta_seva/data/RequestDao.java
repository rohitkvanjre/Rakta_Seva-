package com.example.rakta_seva.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RequestDao {

    @Insert
    void insertRequest(RequestEntity request);

    @Query("SELECT * FROM requests")
    List<RequestEntity> getAllRequests();

    @Query("SELECT * FROM requests WHERE id = :id LIMIT 1")
    RequestEntity getRequestById(int id);

    @Query("UPDATE requests SET status = :status, acceptedByPhone = :phone WHERE id = :id")
    void updateStatus(int id, String status, String phone);

    @Query("SELECT * FROM requests WHERE status = 'pending'")
    List<RequestEntity> getPendingRequests();

    @Query("SELECT * FROM requests WHERE status = 'pending' ORDER BY distanceKm ASC")
    List<RequestEntity> getPendingRequestsByDistance();

    @Query("SELECT * FROM requests WHERE status = 'pending' ORDER BY distanceKm ASC LIMIT :limit")
    List<RequestEntity> getTopPendingRequests(int limit);

    @Query("SELECT * FROM requests WHERE acceptedByPhone = :phone")
    List<RequestEntity> getRequestsByAcceptedPhone(String phone);

    @Query("SELECT * FROM requests WHERE status != 'pending'")
    List<RequestEntity> getRespondedRequests();

    @Query("DELETE FROM requests WHERE id = :id")
    void deleteRequest(int id);
}
