package com.example.cabinetmedical.dao;

import androidx.room.*;
import com.example.cabinetmedical.model.Medecin;
import java.util.List;

@Dao
public interface MedecinDao {
    @Insert
    void insert(Medecin medecin);

    @Update
    void update(Medecin medecin);

    @Delete
    void delete(Medecin medecin);

    @Query("SELECT * FROM medecins")
    List<Medecin> getAllMedecins();

    @Query("SELECT * FROM medecins WHERE id = :id")
    Medecin getMedecinById(long id);
}