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
    private static int id = 1;

    public void printMenu() {
        int option;
        do {
//            ConsoleUtils.clearScreen();\
            System.out.println("\n--- Gestión de Clientes ---");
            System.out.println("1. Agregar cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Buscar cliente por ID");
            System.out.println("0. Volver");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> createClient();
                case 2 -> readAllClients();
                case 3 -> findClientById();
            }
        } while (option != 0);
    }

    private void createClient() {
        System.out.print("Nombre: ");
        String name = scanner.nextLine();
        System.out.print("Apellido: ");
        String lastName = scanner.nextLine();
        System.out.print("Teléfono: ");
        String phone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        Client client = new Client(id, name, lastName, phone, email);
        clientService.saveClient(client);
        id++;
        System.out.println("✔ Cliente agregado.");
    }

    private void readAllClients() {
        List<Client> clients = clientService.listClients();

        System.out.println("--- Lista de Clientes ---");

        if (clients.isEmpty()){
            System.out.println("La lista de clientes está vacía.");
        } else {
            System.out.printf("%-5s | %-15s | %-15s | %-12s | %-20s%n",
                    "ID", "Nombre", "Apellido", "Teléfono", "Email");
            System.out.println("-------------------------------------------------------------------------------");


            for (Client c : clients) {
                System.out.printf("%-5d | %-15s | %-15s | %-12s | %-20s%n",
                        c.getId(), c.getName(), c.getLastName(),
                        c.getPhone(), c.getEmail());
            }
        }
    }

    private void findClientById() {
        System.out.print("ID de cliente: ");
        int idClient = scanner.nextInt();
        scanner.nextLine();
        Client c = clientService.findClientById(idClient);
        if (c != null) {
            System.out.println(c);
        } else {
            System.out.println("Cliente no encontrado.");
        }
    }
}
