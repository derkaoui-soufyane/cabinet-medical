package com.example.cabinetmedical.Admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.database.DatabaseHelper;

public class ajouterP extends AppCompatActivity {

    EditText etNom, etPrenom, etEmail, etnum_c;
    Button btnSave;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter);

        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etEmail = findViewById(R.id.etEmail);
        etnum_c = findViewById(R.id.etnum_c);
        btnSave = findViewById(R.id.btnSave);

        db = new DatabaseHelper(this);

        btnSave.setOnClickListener(v -> {

            String nom = etNom.getText().toString().trim();
            String prenom = etPrenom.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String numCarte = etnum_c.getText().toString().trim();

            if (nom.isEmpty() ||
                    prenom.isEmpty() ||
                    email.isEmpty() ||
                    numCarte.isEmpty()) {

                Toast.makeText(this,
                        "Remplir tous les champs",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            boolean inserted = db.insertUser(
                    nom,
                    prenom,
                    email,
                    numCarte,
                    "patient"
            );

            if (inserted) {

                Toast.makeText(this,
                        "Patient ajouté",
                        Toast.LENGTH_SHORT).show();

                finish();

            } else {

                Toast.makeText(this,
                        "Erreur insertion",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}