package com.example.cabinetmedical.dao;

import androidx.room.*;
import com.example.cabinetmedical.model.Consultation;
import java.util.List;

@Dao
public interface ConsultationDao {
    @Insert
    void insert(Consultation consultation);

    @Update
    void update(Consultation consultation);

    @Delete
    void delete(Consultation consultation);

    @Query("SELECT * FROM consultations")
    List<Consultation> getAllConsultations();

    @Query("SELECT * FROM consultations WHERE medecinId = :medecinId")
    List<Consultation> getConsultationsByMedecin(long medecinId);
}