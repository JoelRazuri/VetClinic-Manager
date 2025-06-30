package views;

import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);

    public void print() {
        int option;
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Gestión de Clientes");
            System.out.println("2. Gestión de Mascotas");
            System.out.println("3. Gestión de Citas Médicas");
            System.out.println("4. Gestión de Historiales Médicos");
            System.out.println("0. Salir");
            System.out.print("\nSelecciona una opción: ");
            option = scanner.nextInt();

            switch (option) {
                case 1 -> new ClientMenu().printMenu();
                case 2 -> new PetMenu().printMenu();
                case 3 -> new MedicalShiftMenu().printMenu();
                case 4 -> new MedicalHistoryMenu().printMenu();
            }
        } while (option != 0);
    }
}