package views;

import java.util.List;
import java.util.Scanner;
import models.MedicalHistory;
import services.medicalhistory.MedicalHistoryService;
import services.medicalhistory.MedicalHistoryServiceImpl;
import utils.ConsoleUtils;

public class MedicalHistoryMenu {
    private final MedicalHistoryService medicalHistoryService = new MedicalHistoryServiceImpl();
    private final Scanner scanner = new Scanner(System.in);

    public void printMenu() {
        int option;
        do {
            ConsoleUtils.clearAndShowHeader("Gestión de Historiales Médicos");
            System.out.println("1. Agregar historial médico");
            System.out.println("2. Listar todos los historiales médicos");
            System.out.println("3. Buscar historial médico por ID");
            System.out.println("4. Actualizar historial médico");
            System.out.println("5. Eliminar historial médico");
            System.out.println("6. Listar historiales por mascota");
            System.out.println("0. Volver");
            System.out.print("\nSelecciona una opción: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> createMedicalHistory();
                case 2 -> readAllMedicalHistories();
                case 3 -> readMedicalHistory();
                case 4 -> updateMedicalHistory();
                case 5 -> deleteMedicalHistory();
                case 6 -> findHistoriesByPet();
                case 0 -> ConsoleUtils.clearScreen();
            }
        } while (option != 0);
    }

    private void createMedicalHistory() {
        ConsoleUtils.clearAndShowHeader("Agregar Nuevo Historial Médico");
        medicalHistoryService.createMedicalHistory();
        ConsoleUtils.pressEnterToContinue();
    }

    private void readAllMedicalHistories() {
        ConsoleUtils.clearAndShowHeader("Lista de Todos los Historiales Médicos");

        List<MedicalHistory> medicalHistories = medicalHistoryService.readAllMedicalHistories();

        if (medicalHistories.isEmpty()) {
            System.out.println("❌ No hay historiales médicos registrados.");
        } else {
            System.out.printf("%-5s | %-12s | %-15s | %-15s | %-15s | %-8s%n",
                    "ID", "Fecha", "Descripción", "Tratamiento", "Vacunas", "Pet ID");
            System.out.println("-".repeat(85));

            for (MedicalHistory mh : medicalHistories) {
                System.out.printf("%-5d | %-12s | %-15s | %-15s | %-15s | %-8d%n",
                        mh.getId(), mh.getDate(), mh.getDescription(), mh.getTreatment(),
                        (mh.getVaccines() != null && !mh.getVaccines().isEmpty()) ? mh.getVaccines() : "N/A",
                        mh.getIdPet());
            }
        }

        ConsoleUtils.pressEnterToContinue();
    }

    private void readMedicalHistory() {
        ConsoleUtils.clearAndShowHeader("Buscar Historial Médico");

        System.out.print("Ingresa el ID del historial médico: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        MedicalHistory medicalHistory = medicalHistoryService.readMedicalHistory(id);
        ConsoleUtils.showSeparator();

        if (medicalHistory != null) {
            System.out.println("✔ Historial médico encontrado:");
            System.out.println(medicalHistory);
        } else {
            System.out.println("❌ Historial médico no encontrado con ID: " + id);
        }

        ConsoleUtils.pressEnterToContinue();
    }

    private void updateMedicalHistory() {
        ConsoleUtils.clearAndShowHeader("Actualizar Historial Médico");
        medicalHistoryService.updateMedicalHistory(0);
        ConsoleUtils.pressEnterToContinue();
    }

    private void deleteMedicalHistory() {
        ConsoleUtils.clearAndShowHeader("Eliminar Historial Médico");
        medicalHistoryService.deleteMedicalHistory(0);
        ConsoleUtils.pressEnterToContinue();
    }

    private void findHistoriesByPet() {
        ConsoleUtils.clearAndShowHeader("Historiales Médicos por Mascota");
        medicalHistoryService.findHistoriesByPet(0);
        ConsoleUtils.pressEnterToContinue();
    }
}