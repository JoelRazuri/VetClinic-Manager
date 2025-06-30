package services.medicalhistory;

import models.MedicalHistory;
import java.util.List;

public interface MedicalHistoryService {
    void createMedicalHistory();
    List<MedicalHistory> readAllMedicalHistories();
    MedicalHistory readMedicalHistory(int id);
    void updateMedicalHistory(int id);
    void deleteMedicalHistory(int id);
    List<MedicalHistory> findHistoriesByPet(int idPet);
}