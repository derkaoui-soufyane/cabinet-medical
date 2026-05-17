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
import com.example.cabinetmedical.database.DatabaseHelper;
import com.example.cabinetmedical.patient.patientActivity;

public class MainActivity extends AppCompatActivity {

    EditText email, password;
    Button loginBtn;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        email = findViewById(R.id.emailInput);
        password = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v -> {

            String emailTxt = email.getText().toString().trim();
            String passwordTxt = password.getText().toString().trim();

            if (emailTxt.isEmpty() || passwordTxt.isEmpty()) {
                Toast.makeText(this,
                        "Veuillez remplir tous les champs",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔥 LOGIN FROM SQLITE
            user u = db.login(emailTxt, passwordTxt);

            if (u != null) {

                Toast.makeText(this,
                        "Bienvenue " + u.getRole(),
                        Toast.LENGTH_SHORT).show();

                // ADMIN
                if ("medecin".equalsIgnoreCase(u.getRole())) {

                    Intent intent = new Intent(MainActivity.this,
                            AdminActivity.class);

                    intent.putExtra("email", u.getEmail());
                    intent.putExtra("role", u.getRole());
                    intent.putExtra("id", u.getId());

                    startActivity(intent);
                    finish();
                }

                // PATIENT
                else if ("patient".equalsIgnoreCase(u.getRole())) {

                    Intent intent = new Intent(MainActivity.this,
                            patientActivity.class);
                    intent.putExtra("id", u.getId());
                    intent.putExtra("email", u.getEmail());
                    intent.putExtra("role", u.getRole());
                    intent.putExtra("nom", u.getNom());
                    intent.putExtra("prenom", u.getPrenom());

                    startActivity(intent);
                    finish();
                }

            } else {
                Toast.makeText(this,
                        "Email ou mot de passe incorrect ❌",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}