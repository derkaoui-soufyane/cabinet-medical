package com.example.cabinetmedical.viewmodel;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.cabinetmedical.dao.AppDatabase;
import com.example.cabinetmedical.dao.MedecinDao;
import com.example.cabinetmedical.dao.ConsultationDao;

import com.example.cabinetmedical.model.Medecin;
import com.example.cabinetmedical.model.Consultation;

import java.util.List;

public class MedicalViewModel extends AndroidViewModel {

    private final MedecinDao medecinDao;
    private final ConsultationDao consultationDao;

    public MedicalViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        this.medecinDao = db.medecinDao();
        this.consultationDao = db.consultationDao();
    }

    // ==========================================
    // SERVICES MÉTIER : MÉDECIN (CRUD + VALIDATIONS)
    // ==========================================

    public String insertMedecin(Medecin medecin) {
        // Validation Métier 1 : Vérifier les champs vides
        if (medecin.getNom().trim().isEmpty() || medecin.getPrenom().trim().isEmpty() || medecin.getSpecialite().trim().isEmpty()) {
            return "Erreur : Tous les champs sont obligatoires !";
        }

        // Validation Métier 2 : Regex Téléphone Marocain (05/06/07 suivi de 8 chiffres)
        if (!medecin.getTelephone().matches("^(\\+212|0)[5-7]\\d{8}$")) {
            return "Erreur : Numéro de téléphone marocain invalide !";
        }

        // Si tout est OK  Insertion (CREATE)
        medecinDao.insert(medecin);
        return "Succès : Médecin enregistré avec succès !";
    }

    public List<Medecin> getAllMedecins() {
        return medecinDao.getAllMedecins(); // READ ALL
    }

    public void updateMedecin(Medecin medecin) {
        medecin.setUpdatedAt(System.currentTimeMillis()); // Mise à jour de la date d'audit
        medecinDao.update(medecin); // UPDATE
    }

    public void deleteMedecin(Medecin medecin) {
        medecinDao.delete(medecin); // DELETE
    }

    // ==========================================
    // SERVICES MÉTIER : CONSULTATION (CRUD + VALIDATIONS)
    // ==========================================

    public String insertConsultation(Consultation consultation) {
        // Validation Métier : Vérifier que le diagnostic et le traitement ne sont pas vides
        if (consultation.getDiagnostic().trim().isEmpty() || consultation.getTraitement().trim().isEmpty()) {
            return "Erreur : Le diagnostic et le traitement sont obligatoires !";
        }

        consultationDao.insert(consultation); // CREATE
        return "Succès : Consultation enregistrée !";
    }

    public List<Consultation> getAllConsultations() {
        return consultationDao.getAllConsultations(); // READ ALL
    }
}