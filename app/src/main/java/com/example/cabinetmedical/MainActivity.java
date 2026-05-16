package com.example.cabinetmedical;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnPatients;
    private Button btnNouveauMedecin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gestion des barres système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Liaison des boutons graphiques
        btnPatients = findViewById(R.id.btnPatients);
        btnNouveauMedecin = findViewById(R.id.btnNouveauMedecin);

        // Actions au clic
        btnPatients.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Ouverture : Gestion des Patients", Toast.LENGTH_SHORT).show();
        });

        btnNouveauMedecin.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Ouverture : Formulaire Nouveau Médecin", Toast.LENGTH_SHORT).show();
        });
    }
}