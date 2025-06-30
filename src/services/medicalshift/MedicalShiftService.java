package services.medicalshift;

import models.MedicalShift;
import java.util.List;

public interface MedicalShiftService {
    void createMedicalShift();
    List<MedicalShift> readAllMedicalShift();
    MedicalShift readMedicalShift(int id);
    void updateMedicalShift(int id);
    void deleteMedicalShift(int id);
    List<MedicalShift> getMedicalShiftByClient(int idClient);
}