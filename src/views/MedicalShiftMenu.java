package views;

import java.util.List;
import java.util.Scanner;
import models.MedicalShift;
import services.medicalshift.MedicalShiftService;
import services.medicalshift.MedicalShiftServiceImpl;
import utils.ConsoleUtils;

public class MedicalShiftMenu {
    private final MedicalShiftService medicalShiftService = new MedicalShiftServiceImpl();
    private final Scanner scanner = new Scanner(System.in);

    public void printMenu() {
        int option;
        do {
            ConsoleUtils.clearAndShowHeader("Gestión de Citas Médicas");
            System.out.println("1. Agregar cita médica");
            System.out.println("2. Listar todas las citas médicas");
            System.out.println("3. Buscar cita médica por ID");
            System.out.println("4. Actualizar cita médica");
            System.out.println("5. Eliminar cita médica");
            System.out.println("6. Listar citas médicas por cliente");
            System.out.println("0. Volver");
            System.out.print("\nSelecciona una opción: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> createMedicalShift();
                case 2 -> readAllMedicalShift();
                case 3 -> readMedicalShift();
                case 4 -> updateMedicalShift();
                case 5 -> deleteMedicalShift();
                case 6 -> getMedicalShiftByClient();
                case 0 -> ConsoleUtils.clearScreen();
            }
        } while (option != 0);
    }

    private void createMedicalShift() {
        ConsoleUtils.clearAndShowHeader("Agregar Nueva Cita Médica");
        medicalShiftService.createMedicalShift();
        ConsoleUtils.pressEnterToContinue();
    }

    private void readAllMedicalShift() {
        ConsoleUtils.clearAndShowHeader("Lista de Todas las Citas Médicas");

        List<MedicalShift> medicalShifts = medicalShiftService.readAllMedicalShift();

        if (medicalShifts.isEmpty()) {
            System.out.println("❌ No hay citas médicas registradas.");
        } else {
            System.out.printf("%-5s | %-30s | %-12s | %-10s | %-12s | %-8s | %-10s%n",
                    "ID", "Motivo", "Fecha", "Precio", "Estado", "Pet ID", "Cliente ID");
            System.out.println("-".repeat(90));

            for (MedicalShift ms : medicalShifts) {
                System.out.printf("%-5d | %-30s | %-12s | $%-9.2f | %-12s | %-8d | %-10d%n",
                        ms.getId(), ms.getReason(), ms.getDate(), ms.getPrice(),
                        ms.getStatus(), ms.getIdPet(), ms.getIdClient());
            }
        }

        ConsoleUtils.pressEnterToContinue();
    }

    private void readMedicalShift() {
        ConsoleUtils.clearAndShowHeader("Buscar Cita Médica");

        System.out.print("Ingresa el ID de la cita médica: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        MedicalShift medicalShift = medicalShiftService.readMedicalShift(id);
        ConsoleUtils.showSeparator();

        if (medicalShift != null) {
            System.out.println("✔ Cita médica encontrada:");
            System.out.println(medicalShift);
        } else {
            System.out.println("❌ Cita médica no encontrada con ID: " + id);
        }

        ConsoleUtils.pressEnterToContinue();
    }

    private void updateMedicalShift() {
        ConsoleUtils.clearAndShowHeader("Actualizar Cita Médica");
        medicalShiftService.updateMedicalShift(0); // Pasar 0 para que muestre la lista
        ConsoleUtils.pressEnterToContinue();
    }

    private void deleteMedicalShift() {
        ConsoleUtils.clearAndShowHeader("Eliminar Cita Médica");
        medicalShiftService.deleteMedicalShift(0); // Pasar 0 para que muestre la lista
        ConsoleUtils.pressEnterToContinue();
    }

    private void getMedicalShiftByClient() {
        ConsoleUtils.clearAndShowHeader("Citas Médicas por Cliente");
        medicalShiftService.getMedicalShiftByClient(0); // El método maneja la selección del cliente internamente
        ConsoleUtils.pressEnterToContinue();
    }
}