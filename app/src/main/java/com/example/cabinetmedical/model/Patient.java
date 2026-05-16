package com.example.cabinetmedical.model;

public class Patient {
    private int id;
    private String nom;
    private String prenom;
    private String telephone;
    private String maladie;

    // Constructeur
    public Patient(int id, String nom, String prenom, String telephone, String maladie) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.maladie = maladie;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getMaladie() { return maladie; }
    public void setMaladie(String maladie) { this.maladie = maladie; }
}