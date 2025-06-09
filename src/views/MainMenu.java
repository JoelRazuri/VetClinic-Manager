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
            System.out.println("0. Salir");
            option = scanner.nextInt();

            switch (option) {
                case 1 -> new ClientMenu().printMenu();
                case 2 -> new PetMenu().printMenu();
            }
        } while (option != 0);
    }
}

