package com.example.rakta_seva.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DonorDao {

    @Insert
    void insertDonor(DonorEntity donor);

    @Query("SELECT * FROM donors")
    List<DonorEntity> getAllDonors();

    @Query("SELECT * FROM donors WHERE phone = :phone LIMIT 1")
    DonorEntity getDonorByPhone(String phone);

    @Query("UPDATE donors SET isAvailable = :available WHERE phone = :phone")
    void updateAvailability(String phone, boolean available);

    @Query("SELECT * FROM donors WHERE bloodGroup = :bloodGroup")
    List<DonorEntity> getDonorsByBloodGroup(String bloodGroup);

    @Query("SELECT * FROM donors WHERE isAvailable = 1")
    List<DonorEntity> getAvailableDonors();

    @Query("SELECT * FROM donors WHERE bloodGroup = :bloodGroup AND isAvailable = 1")
    List<DonorEntity> getAvailableDonorsByBloodGroup(String bloodGroup);

    @Query("SELECT * FROM donors WHERE name LIKE '%' || :query || '%' OR bloodGroup LIKE '%' || :query || '%'")
    List<DonorEntity> searchDonors(String query);

    @Update
    void updateDonor(DonorEntity donor);
}
