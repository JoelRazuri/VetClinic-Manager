package views;

import java.util.List;
import java.util.Scanner;
import models.Pet;
import models.Client;
import services.pet.PetService;
import services.pet.PetServiceImpl;
import services.client.ClientService;
import services.client.ClientServiceImpl;


public class PetMenu {
    private final PetService petService = new PetServiceImpl();
    private final ClientService clientService = new ClientServiceImpl();
    private final Scanner scanner = new Scanner(System.in);
    private static int id = 1;

    public void printMenu() {
        int option;
        do {
            System.out.println("\n--- Gestión de Mascotas ---");
            System.out.println("1. Agregar mascota");
            System.out.println("2. Listar todas las mascotas");
            System.out.println("3. Listar mascotas por cliente");
            System.out.println("0. Volver");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> createPet();
                case 2 -> readAllPets();
                case 3 -> listPetsByClient();
            }
        } while (option != 0);
    }

    private void createPet() {
        System.out.print("Nombre: ");
        String name = scanner.nextLine();
        System.out.print("Especie: ");
        String species = scanner.nextLine();
        System.out.print("Raza: ");
        String race = scanner.nextLine();
        System.out.print("Género: ");
        String gender = scanner.nextLine();
        System.out.print("Edad: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("ID del cliente propietario: ");
        int idClient = scanner.nextInt();
        scanner.nextLine();

        Pet pet = new Pet(id, name, species, race, gender, age, idClient);
        petService.savePet(pet);
        id++;
        System.out.println("✔ Mascota agregada.");
    }

    private void readAllPets() {
        List<Pet> pets = petService.listPets();

        System.out.println("---- Lista de Todas las Mascotas ----");

        if (pets.isEmpty()) {
            System.out.println("La lista de mascotas está vacía.");
        } else {
            System.out.printf("%-5s | %-12s | %-10s | %-12s | %-9s | %-5s | %-10s%n",
                    "ID", "Nombre", "Especie", "Raza", "Género", "Edad", "ID Cliente");
            System.out.println("----------------------------------------------------------------------------");
            for (Pet p : pets) {
                System.out.printf("%-5d | %-12s | %-10s | %-12s | %-9s | %-5d | %-10d%n",
                        p.getId(), p.getName(), p.getSpecies(), p.getRace(),
                        p.getGender(), p.getAge(), p.getIdClient());
            }
        }
    }

    private void listPetsByClient() {
        System.out.print("ID del cliente propietario: ");
        int idClient = scanner.nextInt();
        scanner.nextLine();

        Client client = clientService.findClientById(idClient);
        if (client == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        System.out.println("---- Mascotas del Cliente ----");
        System.out.println("Cliente: " + client.getName() + " " + client.getLastName());
        System.out.printf("%-5s | %-12s | %-10s | %-16s | %-8s | %-5s%n",
                "ID", "Nombre", "Especie", "Raza", "Género", "Edad");
        System.out.println("---------------------------------------------------------------");

        for (Pet p : petService.listPetsByClient(idClient)) {
            System.out.printf("%-5d | %-12s | %-10s | %-16s | %-8s | %-5d%n",
                    p.getId(), p.getName(), p.getSpecies(), p.getRace(),
                    p.getGender(), p.getAge());
        }
    }
}
