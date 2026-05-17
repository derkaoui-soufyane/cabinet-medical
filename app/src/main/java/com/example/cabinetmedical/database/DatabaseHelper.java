package com.example.cabinetmedical.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.cabinetmedical.Entity.user;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "cabinet.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // ================= CREATE =================
    @Override
    public void onCreate(SQLiteDatabase db) {

        // ===== USER =====
        db.execSQL(
                "CREATE TABLE user (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nom TEXT," +
                        "prenom TEXT," +
                        "email TEXT UNIQUE," +
                        "password TEXT," +
                        "role TEXT)"
        );

        // ===== CONSULTATION =====
        db.execSQL(
                "CREATE TABLE consultation (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "patient_id INTEGER," +
                        "medcin_id INTEGER," +
                        "description TEXT," +
                        "date TEXT," +
                        "prix REAL)"
        );

        // ===== FACTURE =====
        db.execSQL(
                "CREATE TABLE facture (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "consultation_id INTEGER," +
                        "montant REAL," +
                        "status TEXT)"
        );

        insertDefaultData(db);
    }

    // ================= DEFAULT DATA =================
    private void insertDefaultData(SQLiteDatabase db) {

        db.execSQL("INSERT INTO user(nom,prenom,email,password,role) VALUES('Admin','System','admin@gmail.com','1234','admin')");
        db.execSQL("INSERT INTO user(nom,prenom,email,password,role) VALUES('Ali','Patient','patient@gmail.com','1234','patient')");
        db.execSQL("INSERT INTO user(nom,prenom,email,password,role) VALUES('Dr Ahmed','Medecin','doc@gmail.com','1234','medecin')");

        db.execSQL("INSERT INTO consultation(patient_id,medcin_id,description,date,prix) VALUES(2,3,'Consultation générale','2026-05-17',200)");
        db.execSQL("INSERT INTO consultation(patient_id,medcin_id,description,date,prix) VALUES(2,3,'Douleur ventre','2026-05-18',150)");

        db.execSQL("INSERT INTO facture(consultation_id,montant,status) VALUES(1,200,'non payé')");
        db.execSQL("INSERT INTO facture(consultation_id,montant,status) VALUES(2,150,'payé')");
    }

    // ================= LOGIN =================
    public user login(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM user WHERE email=? AND password=?",
                new String[]{email, password}
        );

        if (cursor.moveToFirst()) {

            user u = new user(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );

            cursor.close();
            return u;
        }

        cursor.close();
        return null;
    }

    // ================= INSERT USER =================
    public boolean insertUser(String nom, String prenom,
                              String email, String password,
                              String role) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO user(nom,prenom,email,password,role) VALUES('" +
                nom + "','" + prenom + "','" + email + "','" + password + "','" + role + "')");

        return true;
    }

    // ================= INSERT CONSULTATION =================
    public boolean insertConsultation(int patientId, int medcinId,
                                      String description, String date,
                                      double prix) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO consultation(patient_id,medcin_id,description,date,prix) VALUES(" +
                patientId + "," +
                medcinId + ",'" +
                description + "','" +
                date + "'," +
                prix + ")");

        return true;
    }

    // ================= INSERT FACTURE =================
    public boolean insertFacture(int consultationId, double montant, String status) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO facture(consultation_id,montant,status) VALUES(" +
                consultationId + "," +
                montant + ",'" +
                status + "')");

        return true;
    }

    // ================= CONSULTATIONS BY PATIENT =================

    public Cursor getConsultationsByPatient(int patientId) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT c.id, c.description, c.date, c.prix, " +
                        "m.nom, m.prenom " +
                        "FROM consultation c " +
                        "JOIN user m ON c.medcin_id = m.id " +
                        "WHERE c.patient_id = ?",
                new String[]{String.valueOf(patientId)}
        );
    }
    // ================= CONSULTATIONS BY MEDCIN =================
    public Cursor getConsultationsByMedcin(int medcinId) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT c.id, c.description, c.date, c.prix, " +
                        "p.nom, p.prenom " +
                        "FROM consultation c " +
                        "JOIN user p ON c.patient_id = p.id " +
                        "WHERE c.medcin_id=?",
                new String[]{String.valueOf(medcinId)}
        );
    }
    public Cursor getAllConsultations() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT c.id, c.description, c.date, c.prix, " +
                        "p.nom, p.prenom, " +
                        "m.nom, m.prenom " +
                        "FROM consultation c " +
                        "JOIN user p ON c.patient_id = p.id " +
                        "JOIN user m ON c.medcin_id = m.id",
                null
        );
    }
    public Cursor getConsultationsForMedcin(int medcinId) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT c.id, c.description, c.date, c.prix, " +
                        "p.nom, p.prenom " +
                        "FROM consultation c " +
                        "JOIN user p ON c.patient_id = p.id " +
                        "WHERE c.medcin_id = ?",
                new String[]{String.valueOf(medcinId)}
        );
    }

    // ================= FACTURE BY CONSULTATION =================
    public Cursor getFactureByConsultation(int consultationId) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM facture WHERE consultation_id=?",
                new String[]{String.valueOf(consultationId)}
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS facture");
        db.execSQL("DROP TABLE IF EXISTS consultation");
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
}