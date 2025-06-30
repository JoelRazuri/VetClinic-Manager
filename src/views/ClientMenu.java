package views;

import java.util.List;
import java.util.Scanner;
import models.Client;
import services.client.ClientService;
import services.client.ClientServiceImpl;
import utils.ConsoleUtils;

public class ClientMenu {
    private final ClientService clientService = new ClientServiceImpl();
    private final Scanner scanner = new Scanner(System.in);

    public void printMenu() {
        int option;
        do {
            ConsoleUtils.clearAndShowHeader("Gestión de Clientes");
            System.out.println("1. Agregar cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Buscar cliente por ID");
            System.out.println("4. Actualizar cliente");
            System.out.println("5. Eliminar cliente");
            System.out.println("0. Volver");
            System.out.print("\nSelecciona una opción: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> createClient();
                case 2 -> readAllClients();
                case 3 -> findClientById();
                case 4 -> updateClient();
                case 5 -> deleteClient();
                case 0 -> ConsoleUtils.clearScreen();
            }
        } while (option != 0);
    }

    private void createClient() {
        ConsoleUtils.clearAndShowHeader("Agregar Nuevo Cliente");
        clientService.createClient();
        ConsoleUtils.pressEnterToContinue();
    }

    private void readAllClients() {
        ConsoleUtils.clearAndShowHeader("Lista de Clientes");

        List<Client> clients = clientService.readAllClients();

        if (clients.isEmpty()){
            System.out.println("❌ No hay clientes registrados.");
        } else {
            System.out.printf("%-5s | %-15s | %-15s | %-12s | %-25s%n",
                    "ID", "Nombre", "Apellido", "Teléfono", "Email");
            System.out.println("-".repeat(80));

            for (Client c : clients) {
                System.out.printf("%-5d | %-15s | %-15s | %-12s | %-25s%n",
                        c.getId(), c.getName(), c.getLastName(),
                        c.getPhone(), c.getEmail());
            }
        }

        ConsoleUtils.pressEnterToContinue();
    }

    private void findClientById() {
        ConsoleUtils.clearAndShowHeader("Buscar Cliente");

        System.out.print("Ingresa el ID del cliente: ");
        int idClient = scanner.nextInt();
        scanner.nextLine();

        Client c = clientService.findClientById(idClient);
        ConsoleUtils.showSeparator();

        if (c != null) {
            System.out.println("✔ Cliente encontrado:");
            System.out.println(c);
        } else {
            System.out.println("❌ Cliente no encontrado con ID: " + idClient);
        }

        ConsoleUtils.pressEnterToContinue();
    }

    private void updateClient() {
        ConsoleUtils.clearAndShowHeader("Actualizar Cliente");
        clientService.updateClient(0); // Pasar 0 para que muestre la lista
        ConsoleUtils.pressEnterToContinue();
    }

    private void deleteClient() {
        ConsoleUtils.clearAndShowHeader("Eliminar Cliente");
        clientService.deleteClient(0); // Pasar 0 para que muestre la lista
        ConsoleUtils.pressEnterToContinue();
    }
}