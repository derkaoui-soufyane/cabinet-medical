package com.example.cabinetmedical.dao;

import androidx.lifecycle.LiveData;
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

    // 1. Pour afficher la liste des consultations du médecin connecté
    @Query("SELECT * FROM consultations WHERE medecinId = :medecinId ORDER BY date DESC")
    LiveData<List<Consultation>> getConsultationsByMedecin(long medecinId);

    // 2. Pour alimenter le compteur "Total Consultations"
    @Query("SELECT COUNT(*) FROM consultations WHERE medecinId = :medecinId")
    LiveData<Integer> getConsultationCount(long medecinId);

    // 3. Pour alimenter le compteur "Revenus" (Somme des consultations payées)
    @Query("SELECT SUM(montant) FROM consultations WHERE medecinId = :medecinId AND statutPaiement = 'Payé'")
    LiveData<Double> getTotalRevenus(long medecinId);

    @Query("SELECT * FROM consultations")
    List<Consultation> getAllConsultations();

    @Query("SELECT * FROM consultations WHERE patientId = :patientId ORDER BY date DESC")
    LiveData<List<Consultation>> getConsultationsByPatient(int patientId);
}