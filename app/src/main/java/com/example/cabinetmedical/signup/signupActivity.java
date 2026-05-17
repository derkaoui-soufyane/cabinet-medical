package com.example.cabinetmedical.signup;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.database.DatabaseHelper;

public class signupActivity extends AppCompatActivity {

    EditText nom, prenom, email, password;
    Button btnSignup;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        db = new DatabaseHelper(this);

        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password2);

        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(v -> {

            String nomTxt = nom.getText().toString().trim();
            String prenomTxt = prenom.getText().toString().trim();
            String emailTxt = email.getText().toString().trim();
            String passwordTxt = password.getText().toString().trim();

            // VALIDATION
            if (nomTxt.isEmpty()) {
                nom.setError("Entrer nom");
                return;
            }

            if (prenomTxt.isEmpty()) {
                prenom.setError("Entrer prénom");
                return;
            }

            if (emailTxt.isEmpty()) {
                email.setError("Entrer email");
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()) {
                email.setError("Email invalide");
                return;
            }

            if (passwordTxt.length() < 4) {
                password.setError("Minimum 4 caractères");
                return;
            }

            // INSERT INTO DATABASE
            boolean inserted = db.insertUser(
                    nomTxt,
                    prenomTxt,
                    emailTxt,
                    passwordTxt,
                    "medecin"
            );

            // SUCCESS
            if (inserted) {

                Toast.makeText(this,
                        "Compte créé avec succès ✅",
                        Toast.LENGTH_LONG).show();

                finish();
            }

            // EMAIL EXISTS
            else {

                Toast.makeText(this,
                        "Cet email existe déjà ❌",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}