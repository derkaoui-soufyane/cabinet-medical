package com.example.cabinetmedical.consultation;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.database.DatabaseHelper;

public class ConsultationActivity extends AppCompatActivity {

    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityconsulta);

        container = findViewById(R.id.consultationContainer);

        int patientId = getIntent().getIntExtra("patient_id", -1);
        int medcinId = getIntent().getIntExtra("medcin_id", -1);

        if (patientId == -1 || medcinId == -1) {
            Toast.makeText(this, "IDs invalides", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper db = new DatabaseHelper(this);

        Cursor cursor = db.getConsultationsByPatient(patientId);

        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "Aucune consultation", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cursor.moveToFirst()) {

            do {
                int id = cursor.getInt(0);
                String description = cursor.getString(1);
                String date = cursor.getString(2);
                double prix = cursor.getDouble(3);

                String medNom = cursor.getString(4);
                String medPrenom = cursor.getString(5);

                LinearLayout card = buildConsultationCard(
                        id,
                        description,
                        date,
                        prix,
                        medPrenom + " " + medNom
                );

                container.addView(card);

            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    // ================= CARD =================
    private LinearLayout buildConsultationCard(int id,
                                               String desc,
                                               String date,
                                               double prix,
                                               String medecinName) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(30, 30, 30, 30);
        card.setBackgroundResource(R.drawable.card_dark);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(0, 0, 0, 20);
        card.setLayoutParams(params);

        TextView tvId = new TextView(this);
        tvId.setText("ID: " + id);
        tvId.setTextColor(0xFFAAAAAA);

        TextView tvDesc = new TextView(this);
        tvDesc.setText("🩺 " + desc);
        tvDesc.setTextColor(0xFFFFFFFF);

        TextView tvDate = new TextView(this);
        tvDate.setText("📅 " + date);
        tvDate.setTextColor(0xFFAAAAAA);

        TextView tvPrice = new TextView(this);
        tvPrice.setText("💰 " + prix + " MAD");
        tvPrice.setTextColor(0xFF3E8C74);

        TextView tvMed = new TextView(this);
        tvMed.setText("👨‍⚕️ " + medecinName);
        tvMed.setTextColor(0xFFC8D8DF);

        card.addView(tvId);
        card.addView(tvDesc);
        card.addView(tvDate);
        card.addView(tvPrice);
        card.addView(tvMed);

        return card;
    }
}