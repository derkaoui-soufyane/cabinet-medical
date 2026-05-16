package com.example.cabinetmedical.model;

import androidx.room.Entity;

@Entity(tableName = "medecins")
public class Medecin extends AbstractEntity {

    private String nom;
    private String prenom;
    private String specialite;
    private String telephone;

    public Medecin(String nom, String prenom, String specialite, String telephone) {
        super();
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = specialite;
        this.telephone = telephone;
    }

    // Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}