package com.example.cabinetmedical.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "consultations")
public class Consultation {

    @PrimaryKey(autoGenerate = true)
    private long id; // Room a besoin d'une clé primaire ici

    private long patientId;      // Stocke l'ID du patient (provenant du SQLite de ton binôme)
    private long medecinId;      // Stocke l'ID du médecin connectée
    private String date;
    private String diagnostic;
    private String prescription;
    private String traitement;
    private double montant;
    private String statutPaiement; // "Payé" ou "En attente"

    // Constructeur vide pour Room
    public Consultation() {}

    // Constructeur pratique pour ton activité
    public Consultation(long patientId, long medecinId, String dateConsultation, String diagnostic, String prescription, String traitement, double montantTarif, String statutPaiement) {
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.date = date;
        this.diagnostic = diagnostic;
        this.prescription = prescription;
        this.traitement = traitement;
        this.montant = montant;
        this.statutPaiement = statutPaiement;
    }

    // --- GETTERS & SETTERS ---
    public int getId() { return (int) this.id; }
    public void setId(long id) { this.id = id; }

    public long getMedecinId() { return medecinId; }
    public void setMedecinId(long medecinId) { this.medecinId = medecinId; }

    public String getDate() { return this.date; }
    public void setDate(String date) { this.date = date; }

    public String getDiagnostic() { return diagnostic; }
    public void setDiagnostic(String diagnostic) { this.diagnostic = diagnostic; }

    public String getTraitement() { return traitement; }
    public void setTraitement(String traitement) { this.traitement = traitement; }

    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }

    public long getPatientId() { return patientId; }
    public void setPatientId(long patientId) { this.patientId = patientId; }

    public double getMontant() { return this.montant; }
    public void setMontant(double montant) { this.montant = montant; }

    public String getStatutPaiement() { return statutPaiement; }
    public void setStatutPaiement(String statutPaiement) { this.statutPaiement = statutPaiement; }

}