package com.example.cabinetmedical.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        tableName = "consultations",
        foreignKeys = @ForeignKey(
                entity = Medecin.class,
                parentColumns = "id",
                childColumns = "medecinId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Consultation extends AbstractEntity {

    private long medecinId;
    private String dateConsultation;
    private String diagnostic;
    private String traitement;

    public Consultation(long medecinId, String dateConsultation, String diagnostic, String traitement) {
        super();
        this.medecinId = medecinId;
        this.dateConsultation = dateConsultation;
        this.diagnostic = diagnostic;
        this.traitement = traitement;
    }

    // Getters et Setters
    public long getMedecinId() { return medecinId; }
    public void setMedecinId(long medecinId) { this.medecinId = medecinId; }

    public String getDateConsultation() { return dateConsultation; }
    public void setDateConsultation(String dateConsultation) { this.dateConsultation = dateConsultation; }

    public String getDiagnostic() { return diagnostic; }
    public void setDiagnostic(String diagnostic) { this.diagnostic = diagnostic; }

    public String getTraitement() { return traitement; }
    public void setTraitement(String traitement) { this.traitement = traitement; }
}