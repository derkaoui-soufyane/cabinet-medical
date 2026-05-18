package com.example.cabinetmedical.Admin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.consultation.ConsultationActivity;
import com.example.cabinetmedical.database.DatabaseHelper;

public class AdminActivity extends AppCompatActivity {

    LinearLayout patientContainer, consultationContainer;
    TextView tvCount, tvRevenu, Adminn;
    DatabaseHelper db;
    int medcinId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout1admin);

        // Initialisation de la base de données
        db = new DatabaseHelper(this);

        patientContainer = findViewById(R.id.patientContainer);
        consultationContainer = findViewById(R.id.consultationContainer);

        tvCount = findViewById(R.id.tvStatConsultCount);
        tvRevenu = findViewById(R.id.tvStatRevenu);
        Adminn = findViewById(R.id.Adminn);

        // ================= GET MEDECIN ID =================
        medcinId = getIntent().getIntExtra("id", 0);

        if (medcinId == 0) {
            Toast.makeText(this, "Medcin ID invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        // ================= NOM PRENOM =================
        String nomMedcin = getIntent().getStringExtra("nom");
        String prenomMedcin = getIntent().getStringExtra("prenom");
        Adminn.setText("Bienvenue " + prenomMedcin + " " + nomMedcin);

        // ================= BUTTON AJOUT PATIENT =================
        findViewById(R.id.btnAddPatient).setOnClickListener(v -> {
            Intent i = new Intent(this, ajouterP.class);
            i.putExtra("id", medcinId);
            startActivity(i);
        });
    }

    // ================= CYCLE DE VIE : REFRESH AUTOMATIQUE =================
    @Override
    protected void onResume() {
        super.onResume();
        // À chaque fois qu'on retourne sur cet écran, on rafraîchit les listes
        if (medcinId != 0) {
            chargerDonnees();
        }
    }

    // ================= MÉTHODE DE CHARGEMENT DES DONNÉES =================
    private void chargerDonnees() {
        // IMPORTANT : On vide les anciens affichages pour éviter les doublons au rafraîchissement
        patientContainer.removeAllViews();
        consultationContainer.removeAllViews();

        // ================= CHARGEMENT DES PATIENTS =================
        Cursor cp = db.getPatientsByMedcin(medcinId);

        if (cp != null && cp.moveToFirst()) {
            do {
                int patientId = cp.getInt(0);
                String nom = cp.getString(1);
                String prenom = cp.getString(2);
                String email = cp.getString(3);

                LinearLayout card = new LinearLayout(this);
                card.setOrientation(LinearLayout.VERTICAL);
                card.setPadding(30, 30, 30, 30);
                card.setBackgroundResource(R.drawable.card_dark);

                // Petite astuce de marge pour espacer les cartes
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 20);
                card.setLayoutParams(params);

                TextView name = new TextView(this);
                name.setText("👤 " + prenom + " " + nom);
                name.setTextColor(0xFFFFFFFF);
                name.setTextSize(18);

                TextView mail = new TextView(this);
                mail.setText(email);
                mail.setTextColor(0xFFAAAAAA);

                card.addView(name);
                card.addView(mail);

                // ================= OUVRE LA CONSULTATION =================
                card.setOnClickListener(v -> {
                    Intent i = new Intent(this, ConsultationActivity.class);
                    i.putExtra("patient_id", patientId);
                    i.putExtra("medcin_id", medcinId);
                    startActivity(i);
                });

                patientContainer.addView(card);

            } while (cp.moveToNext());
        }
        if (cp != null) cp.close();


        // ================= CHARGEMENT DES CONSULTATIONS & STATS =================
        Cursor c = db.getConsultationsByMedcin(medcinId);
        int count = 0;
        double total = 0;

        if (c != null && c.moveToFirst()) {
            do {
                count++;
                total += c.getDouble(3);

                String description = c.getString(1);
                String date = c.getString(2);

                LinearLayout card = new LinearLayout(this);
                card.setOrientation(LinearLayout.VERTICAL);
                card.setPadding(30, 30, 30, 30);
                card.setBackgroundResource(R.drawable.card_dark);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 20);
                card.setLayoutParams(params);

                TextView t1 = new TextView(this);
                t1.setText("🩺 " + description);
                t1.setTextColor(0xFFFFFFFF);
                t1.setTextSize(18);

                TextView t2 = new TextView(this);
                t2.setText("📅 " + date);
                t2.setTextColor(0xFFAAAAAA);

                card.addView(t1);
                card.addView(t2);

                consultationContainer.addView(card);

            } while (c.moveToNext());
        }
        if (c != null) c.close();

        // Mise à jour des compteurs statistiques globaux
        tvCount.setText(String.valueOf(count));
        tvRevenu.setText(String.format("%.0f MAD", total));
    }
}