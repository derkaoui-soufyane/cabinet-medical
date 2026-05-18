package com.example.cabinetmedical.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.model.Consultation;
import com.example.cabinetmedical.viewmodel.ConsultationViewModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreerConsultationActivity extends AppCompatActivity {

    private EditText etDiagnostic, etPrescription, etMontant;
    private RadioGroup rgStatut;
    private Button btnValider;

    private ConsultationViewModel consultationViewModel;
    private int patientIdRecu, medcinIdRecu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_consultation);

        // 1. Initialisation des vues
        etDiagnostic = findViewById(R.id.et_diagnostic);
        etPrescription = findViewById(R.id.et_prescription);
        etMontant = findViewById(R.id.et_montant);
        rgStatut = findViewById(R.id.rg_statut);
        btnValider = findViewById(R.id.btn_valider_consultation);

        // 2. Récupération des IDs réels envoyés par l'activité précédente
        patientIdRecu = getIntent().getIntExtra("patient_id", -1);
        medcinIdRecu = getIntent().getIntExtra("medcin_id", -1);

        if (patientIdRecu == -1 || medcinIdRecu == -1) {
            Toast.makeText(this, "Données de session manquantes", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 3. Initialisation du ViewModel Room
        consultationViewModel = new ViewModelProvider(this).get(ConsultationViewModel.class);

        // 4. Action du bouton Valider
        btnValider.setOnClickListener(v -> enregistrerNouvelleConsultation());
    }

    private void enregistrerNouvelleConsultation() {
        String diagnostic = etDiagnostic.getText().toString().trim();
        String prescription = etPrescription.getText().toString().trim();
        String montantStr = etMontant.getText().toString().trim();

        if (diagnostic.isEmpty() || prescription.isEmpty() || montantStr.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        double montant = Double.parseDouble(montantStr);

        int checkedId = rgStatut.getCheckedRadioButtonId();
        String statutPaiement = "En attente";
        if (checkedId == R.id.rb_paye) {
            statutPaiement = "Payé";
        }

        String dateAujourdhui = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Créer l'objet Consultation Room avec les VRAIS IDs reçus de l'IHM
        Consultation nouvelleConsultation = new Consultation(
                patientIdRecu,
                medcinIdRecu,
                dateAujourdhui,
                diagnostic,
                prescription,
                diagnostic,
                montant,
                statutPaiement
        );

        // Insertion magique via Room !
        consultationViewModel.insert(nouvelleConsultation);

        Toast.makeText(this, "Consultation enregistrée avec succès via Room !", Toast.LENGTH_LONG).show();

        // Ferme l'écran actuel, le onResume() de l'écran précédent va se déclencher automatiquement !
        finish();
    }
}