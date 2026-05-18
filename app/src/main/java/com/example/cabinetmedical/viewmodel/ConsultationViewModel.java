package com.example.cabinetmedical.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.cabinetmedical.dao.AppDatabase;
import com.example.cabinetmedical.dao.ConsultationDao;
import com.example.cabinetmedical.model.Consultation;
import java.util.List;

public class ConsultationViewModel extends AndroidViewModel {

    private final ConsultationDao consultationDao;

    public ConsultationViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        this.consultationDao = database.consultationDao();
    }

    public void insert(Consultation consultation) {
        // Grâce à .allowMainThreadQueries() dans AppDatabase, on peut tester l'insertion directement
        new Thread(() -> consultationDao.insert(consultation)).start();
    }

    public LiveData<List<Consultation>> getConsultationsByMedecin(long medecinId) {
        return consultationDao.getConsultationsByMedecin(medecinId);
    }

    public LiveData<Integer> getConsultationCount(long medecinId) {
        return consultationDao.getConsultationCount(medecinId);
    }

    public LiveData<Double> getTotalRevenus(long medecinId) {
        return consultationDao.getTotalRevenus(medecinId);
    }

    public LiveData<List<Consultation>> getConsultationsByPatient(int patientId) {
        return consultationDao.getConsultationsByPatient(patientId);
    }
}