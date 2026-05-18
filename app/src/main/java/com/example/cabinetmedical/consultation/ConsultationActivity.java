package com.example.cabinetmedical.consultation;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.database.DatabaseHelper;
import com.example.cabinetmedical.model.Consultation;
import com.example.cabinetmedical.viewmodel.ConsultationViewModel;

import java.util.List;

public class ConsultationActivity extends AppCompatActivity {

    LinearLayout container;
    TextView tvPatientNom, tvPatientEmail, tvPatientId;
    DatabaseHelper db;
    int patientId, medcinId;

    private ConsultationViewModel consultationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityconsulta);

        db = new DatabaseHelper(this);

        container = findViewById(R.id.consultationContainer);
        tvPatientNom = findViewById(R.id.tvPatientDetailNom);
        tvPatientEmail = findViewById(R.id.tvPatientDetailEmail);
        tvPatientId = findViewById(R.id.tvPatientDetailId);

        patientId = getIntent().getIntExtra("patient_id", -1);
        medcinId = getIntent().getIntExtra("medcin_id", -1);

        if (patientId == -1 || medcinId == -1) {
            Toast.makeText(this, "IDs invalides", Toast.LENGTH_SHORT).show();
            return;
        }

        afficherInfosPatient(patientId);

        consultationViewModel = new ViewModelProvider(this).get(ConsultationViewModel.class);

        findViewById(R.id.btnNouvelleConsultation).setOnClickListener(v -> {
            Intent intentFormulaire = new Intent(this, com.example.cabinetmedical.ui.CreerConsultationActivity.class);
            intentFormulaire.putExtra("patient_id", patientId);
            intentFormulaire.putExtra("medcin_id", medcinId);
            startActivity(intentFormulaire);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (patientId != -1) {
            chargerHistoriqueConsultationsRoom();
        }
    }

    // ================= LECTURE VIA ROOM ORM =================
    private void chargerHistoriqueConsultationsRoom() {
        consultationViewModel.getConsultationsByPatient(patientId).observe(this, consultations -> {

            container.removeAllViews();

            if (consultations == null || consultations.isEmpty()) {
                Toast.makeText(this, "Aucune consultation enregistrée", Toast.LENGTH_SHORT).show();
                return;
            }

            for (Consultation c : consultations) {
                int id = c.getId();
                String diagnostic = c.getDiagnostic();
                String date = c.getDate();
                double montant = c.getMontant();

                LinearLayout card = buildConsultationCard(
                        id,
                        diagnostic,
                        date,
                        montant,
                        "Médecin en session"
                );

                container.addView(card);
            }
        }); // < Fermeture propre de l'observeur LiveData qui manquait !
    }

    // Récupère le profil du patient depuis le SQLite classique de ton binôme
    private void afficherInfosPatient(int patientId) {
        android.database.sqlite.SQLiteDatabase database = db.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT nom, prenom, email FROM user WHERE id = ?", new String[]{String.valueOf(patientId)});

        if (c != null && c.moveToFirst()) {
            String nom = c.getString(0);
            String prenom = c.getString(1);
            String email = c.getString(2);

            tvPatientNom.setText("👤 " + prenom + " " + nom);
            tvPatientEmail.setText("✉️ " + email);
            tvPatientId.setText("ID Patient : #" + patientId);
        } else {
            tvPatientNom.setText("👤 Patient Inconnu");
        }
        if (c != null) c.close();
    }

    // ================= CARD DE CONSULTATION PREMIUM =================
    private LinearLayout buildConsultationCard(int id, String desc, String date, double prix, String medecinName) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(35, 35, 35, 35);
        card.setBackgroundResource(R.drawable.card_dark);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 24);
        card.setLayoutParams(params);

        TextView tvId = new TextView(this);
        tvId.setText("Consultation N° " + id);
        tvId.setTextColor(0xFFA0AEC0);
        tvId.setTextSize(13);

        TextView tvDesc = new TextView(this);
        tvDesc.setText("🩺 " + desc);
        tvDesc.setTextColor(0xFFFFFFFF);
        tvDesc.setTextSize(18);
        tvDesc.setPadding(0, 8, 0, 8);

        TextView tvDate = new TextView(this);
        tvDate.setText("📅 Date : " + date);
        tvDate.setTextColor(0xFFA0AEC0);
        tvDate.setTextSize(15);

        TextView tvPrice = new TextView(this);
        tvPrice.setText("💰 " + String.format(java.util.Locale.US, "%.0f", prix) + " MAD");
        tvPrice.setTextColor(0xFF00CECE);
        tvPrice.setTextSize(20);
        tvPrice.setTypeface(null, android.graphics.Typeface.BOLD);

        card.addView(tvId);
        card.addView(tvDesc);
        card.addView(tvDate);
        card.addView(tvPrice);

        return card;
    }
}