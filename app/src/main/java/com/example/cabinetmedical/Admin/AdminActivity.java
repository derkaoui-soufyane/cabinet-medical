package com.example.cabinetmedical.Admin;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.database.DatabaseHelper;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout1admin);

        LinearLayout container = findViewById(R.id.consultationContainer);
        TextView tvCount = findViewById(R.id.tvStatConsultCount);
        TextView tvRevenu = findViewById(R.id.tvStatRevenu);

        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.getAllConsultations();

        int count = 0;
        double totalRevenu = 0;

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String description = cursor.getString(1);
            String date = cursor.getString(2);
            double prix = cursor.getDouble(3);
            String prenom = cursor.getString(4);
            String nom = cursor.getString(5);

            count++;
            totalRevenu += prix;

            container.addView(buildCard(this, id, prenom + " " + nom, description, date, prix));
        }

        cursor.close();

        tvCount.setText(String.valueOf(count));
        tvRevenu.setText(String.format("%.0f", totalRevenu));
    }

    private LinearLayout buildCard(Context ctx, int id, String patient,
                                   String desc, String date, double prix) {
        int dp8 = dp(ctx, 8), dp12 = dp(ctx, 12), dp14 = dp(ctx, 14);

        // Outer card
        LinearLayout card = new LinearLayout(ctx);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.card_dark);
        card.setPadding(dp14, dp14, dp14, dp14);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, dp(ctx, 10));
        card.setLayoutParams(cardParams);

        // Header row (patient + price)
        LinearLayout header = new LinearLayout(ctx);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);

        // Avatar circle
        TextView avatar = new TextView(ctx);
        String initials = patient.length() >= 2
                ? String.valueOf(patient.charAt(0)) + patient.charAt(patient.indexOf(" ") + 1)
                : "?";
        avatar.setText(initials.toUpperCase());
        avatar.setTextColor(0xFFC8EDE4);
        avatar.setTextSize(12);
        avatar.setGravity(Gravity.CENTER);
        avatar.setBackgroundResource(R.drawable.circle_teal);
        LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(dp(ctx, 36), dp(ctx, 36));
        avatarParams.setMarginEnd(dp(ctx, 10));
        avatar.setLayoutParams(avatarParams);

        // Name + ID column
        LinearLayout nameCol = new LinearLayout(ctx);
        nameCol.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams nameColParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        nameCol.setLayoutParams(nameColParams);

        TextView tvName = new TextView(ctx);
        tvName.setText(patient);
        tvName.setTextColor(0xFFEFEFEF);
        tvName.setTextSize(14);

        TextView tvId = new TextView(ctx);
        tvId.setText("ID #" + id);
        tvId.setTextColor(0xFF7A9BAB);
        tvId.setTextSize(11);

        nameCol.addView(tvName);
        nameCol.addView(tvId);

        // Price badge
        TextView tvPrice = new TextView(ctx);
        tvPrice.setText(String.format("%.0f MAD", prix));
        tvPrice.setTextColor(0xFF3E8C74);
        tvPrice.setTextSize(13);
        tvPrice.setBackgroundResource(R.drawable.badge_green);
        tvPrice.setPadding(dp(ctx, 10), dp8 / 2, dp(ctx, 10), dp8 / 2);

        header.addView(avatar);
        header.addView(nameCol);
        header.addView(tvPrice);

        // Description
        TextView tvDescLabel = new TextView(ctx);
        tvDescLabel.setText("Description");
        tvDescLabel.setTextColor(0xFF7A9BAB);
        tvDescLabel.setTextSize(11);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        labelParams.setMargins(0, dp12, 0, 0);
        tvDescLabel.setLayoutParams(labelParams);

        TextView tvDesc = new TextView(ctx);
        tvDesc.setText(desc);
        tvDesc.setTextColor(0xFFC8D8DF);
        tvDesc.setTextSize(13);

        // Date
        TextView tvDate = new TextView(ctx);
        tvDate.setText("📅  " + date);
        tvDate.setTextColor(0xFF7A9BAB);
        tvDate.setTextSize(12);
        LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dateParams.setMargins(0, dp8, 0, 0);
        tvDate.setLayoutParams(dateParams);

        card.addView(header);
        card.addView(tvDescLabel);
        card.addView(tvDesc);
        card.addView(tvDate);

        return card;
    }

    private int dp(Context ctx, int value) {
        return Math.round(value * ctx.getResources().getDisplayMetrics().density);
    }
}