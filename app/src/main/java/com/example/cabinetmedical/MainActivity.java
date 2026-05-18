package com.example.cabinetmedical;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cabinetmedical.Admin.AdminActivity;
import com.example.cabinetmedical.Entity.user;
import com.example.cabinetmedical.signup.signupActivity;
import com.example.cabinetmedical.database.DatabaseHelper;
import com.example.cabinetmedical.patient.patientActivity;

public class MainActivity extends AppCompatActivity {

    private EditText email, password;
    private Button loginBtn, signupBtn;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Activation du mode plein écran (Edge-to-Edge)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialisation du gestionnaire de base de données SQLite classique
        db = new DatabaseHelper(this);

        // Liaison des composants graphiques (IHM)
        email = findViewById(R.id.emailInput);
        password = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);

        // ================= BOUTON : S'INSCRIRE =================
        signupBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, signupActivity.class);
            startActivity(intent);
        });

        // ================= BOUTON : SE CONNECTER =================
        loginBtn.setOnClickListener(v -> {
            String emailTxt = email.getText().toString().trim();
            String passwordTxt = password.getText().toString().trim();

            // Vérification des champs vides
            if (emailTxt.isEmpty() || passwordTxt.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tentative de connexion via la table 'user' du SQLite
            user u = db.login(emailTxt, passwordTxt);

            if (u != null) {
                Toast.makeText(this, "Bienvenue " + u.getNom() + " (" + u.getRole() + ")", Toast.LENGTH_SHORT).show();

                // REDIRECTION SI MÉDECIN OU ADMIN ➔ VERS LE DASHBOARD DU MÉDECIN
                if ("medecin".equalsIgnoreCase(u.getRole()) || "admin".equalsIgnoreCase(u.getRole())) {
                    Intent intent = new Intent(MainActivity.this, AdminActivity.class);

                    // Transmission propre de toutes les données de session nécessaires
                    intent.putExtra("id", u.getId());
                    intent.putExtra("nom", u.getNom());
                    intent.putExtra("prenom", u.getPrenom());
                    intent.putExtra("email", u.getEmail());
                    intent.putExtra("role", u.getRole());

                    startActivity(intent);
                    finish(); // Empêche le retour en arrière vers l'écran de Login
                }

                // REDIRECTION SI PATIENT ➔ VERS L'INTERFACE PATIENT
                else if ("patient".equalsIgnoreCase(u.getRole())) {
                    Intent intent = new Intent(MainActivity.this, patientActivity.class);

                    // Transmission des détails du patient
                    intent.putExtra("id", u.getId());
                    intent.putExtra("email", u.getEmail());
                    intent.putExtra("role", u.getRole());
                    intent.putExtra("nom", u.getNom());
                    intent.putExtra("prenom", u.getPrenom());

                    startActivity(intent);
                    finish(); // Empêche le retour en arrière vers l'écran de Login
                }

            } else {
                // Échec de la correspondance email/password
                Toast.makeText(this, "Email ou mot de passe incorrect ❌", Toast.LENGTH_SHORT).show();
            }
        });
    }
}