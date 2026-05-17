package com.example.cabinetmedical.patient;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.database.DatabaseHelper;

public class patientActivity extends AppCompatActivity {

    // ================= COLORS =================
    private static final int DARK   = Color.parseColor("#324047");
    private static final int LIGHT  = Color.parseColor("#EFEFEF");
    private static final int TEAL   = Color.parseColor("#00CECE");
    private static final int WHITE  = Color.WHITE;
    private static final int TEXT_GRAY = Color.parseColor("#555555");

    private int patientId;
    private String email;
    private TextView totalPriceText;
    private TextView welcomeText, emailText;
    private LinearLayout container;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient);

        initData();
        initViews();
        loadConsultations();
    }

    // ---------- INIT ----------
    private void initData() {
        Intent intent = getIntent();
        patientId = intent.getIntExtra("id", 0);
        email = intent.getStringExtra("email");
        db = new DatabaseHelper(this);
    }

    private void initViews() {
        totalPriceText = findViewById(R.id.totalPriceText);
        welcomeText = findViewById(R.id.welcomeText);
        emailText = findViewById(R.id.emailText);
        container = findViewById(R.id.consultationsContainer);

        welcomeText.setText("Bienvenue, Patient");
        emailText.setText(email != null ? email : "");
    }

    // ---------- LOAD DATA ----------
    private void loadConsultations() {
        Cursor cursor = db.getConsultationsByPatient(patientId);

        double total = 0;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                container.addView(buildCard(cursor));

                total += cursor.getDouble(3); // prix

            } while (cursor.moveToNext());

            cursor.close();
        } else {
            container.addView(emptyView());
        }

        totalPriceText.setText(String.format("%.2f MAD", total));
    }

    // ---------- CARD UI ----------
    private View buildCard(Cursor cursor) {

        int id = cursor.getInt(0);
        String desc = cursor.getString(1);
        String date = cursor.getString(2);
        double prix = cursor.getDouble(3);
        String prenom = cursor.getString(4);
        String nom = cursor.getString(5);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(WHITE);
        card.setPadding(dp(0), dp(0), dp(0), dp(0));

        card.addView(createTopBar());
        card.addView(createBody(id, desc, date, prix, prenom, nom));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, dp(12));
        card.setLayoutParams(params);

        return card;
    }

    private View createTopBar() {
        View bar = new View(this);
        bar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(4)));
        bar.setBackgroundColor(TEAL);
        return bar;
    }

    private LinearLayout createBody(int id, String desc, String date,
                                    double prix, String prenom, String nom) {

        LinearLayout body = new LinearLayout(this);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setPadding(dp(16), dp(12), dp(16), dp(16));

        body.addView(headerRow(id, prenom, nom));
        body.addView(description(desc));
        body.addView(divider());
        body.addView(bottomRow(date, prix));

        return body;
    }

    // ---------- ROWS ----------
    private LinearLayout headerRow(int id, String prenom, String nom) {

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);

        TextView badge = new TextView(this);
        badge.setText("#" + id);
        badge.setBackgroundColor(TEAL);
        badge.setTextColor(WHITE);
        badge.setTypeface(null, Typeface.BOLD);
        badge.setPadding(dp(8), dp(4), dp(8), dp(4));

        TextView doctor = new TextView(this);
        doctor.setText("Dr. " + prenom + " " + nom);
        doctor.setTextColor(DARK);
        doctor.setTypeface(null, Typeface.BOLD);
        doctor.setPadding(dp(8), 0, 0, 0);

        row.addView(badge);
        row.addView(doctor);

        return row;
    }

    private TextView description(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(TEXT_GRAY);
        tv.setPadding(0, dp(8), 0, 0);
        return tv;
    }

    private View divider() {
        View v = new View(this);
        v.setBackgroundColor(LIGHT);
        v.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(1)));
        return v;
    }

    private LinearLayout bottomRow(String date, double prix) {

        LinearLayout row = new LinearLayout(this);

        TextView dateTv = new TextView(this);
        dateTv.setText("📅 " + date);
        dateTv.setTextColor(Color.GRAY);
        dateTv.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        TextView priceTv = new TextView(this);
        priceTv.setText(String.format("%.2f MAD", prix));
        priceTv.setTextColor(TEAL);
        priceTv.setTypeface(null, Typeface.BOLD);

        row.addView(dateTv);
        row.addView(priceTv);

        return row;
    }

    // ---------- EMPTY ----------
    private TextView emptyView() {
        TextView tv = new TextView(this);
        tv.setText("Aucune consultation trouvée.");
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.GRAY);
        tv.setPadding(0, dp(20), 0, dp(20));
        return tv;
    }

    // ---------- UTIL ----------
    private int dp(int v) {
        return Math.round(v * getResources().getDisplayMetrics().density);
    }
}