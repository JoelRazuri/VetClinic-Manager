package views;

import java.util.List;
import java.util.Scanner;
import models.Pet;
import models.Client;
import services.pet.PetService;
import services.pet.PetServiceImpl;
import services.client.ClientService;
import services.client.ClientServiceImpl;
import utils.ConsoleUtils;

public class PetMenu {
    private final PetService petService = new PetServiceImpl();
    private final ClientService clientService = new ClientServiceImpl();
    private final Scanner scanner = new Scanner(System.in);

    public void printMenu() {
        int option;
        do {
            ConsoleUtils.clearAndShowHeader("Gestión de Mascotas");
            System.out.println("1. Agregar mascota");
            System.out.println("2. Listar todas las mascotas");
            System.out.println("3. Buscar mascota por ID");
            System.out.println("4. Actualizar mascota");
            System.out.println("5. Eliminar mascota");
            System.out.println("6. Listar mascotas por cliente");
            System.out.println("0. Volver");
            System.out.print("\nSelecciona una opción: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> createPet();
                case 2 -> readAllPets();
                case 3 -> findPetById();
                case 4 -> updatePet();
                case 5 -> deletePet();
                case 6 -> listPetsByClient();
                case 0 -> ConsoleUtils.clearScreen();
            }
        } while (option != 0);
    }

    private void createPet() {
        ConsoleUtils.clearAndShowHeader("Agregar Nueva Mascota");
        petService.createPet();
        ConsoleUtils.pressEnterToContinue();
    }

    private void readAllPets() {
        ConsoleUtils.clearAndShowHeader("Lista de Todas las Mascotas");

        List<Pet> pets = petService.readAllPets();

        if (pets.isEmpty()) {
            System.out.println("❌ No hay mascotas registradas.");
        } else {
            System.out.printf("%-5s | %-12s | %-10s | %-12s | %-9s | %-5s | %-10s%n",
                    "ID", "Nombre", "Especie", "Raza", "Género", "Edad", "ID Cliente");
            System.out.println("-".repeat(80));

            for (Pet p : pets) {
                System.out.printf("%-5d | %-12s | %-10s | %-12s | %-9s | %-5d | %-10d%n",
                        p.getId(), p.getName(), p.getSpecies(), p.getRace(),
                        p.getGender(), p.getAge(), p.getIdClient());
            }
        }

        ConsoleUtils.pressEnterToContinue();
    }

    private void findPetById() {
        ConsoleUtils.clearAndShowHeader("Buscar Mascota");

        System.out.print("Ingresa el ID de la mascota: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Pet pet = petService.findPetById(id);
        ConsoleUtils.showSeparator();

        if (pet != null) {
            System.out.println("✔ Mascota encontrada:");
            System.out.println(pet);
        } else {
            System.out.println("❌ Mascota no encontrada con ID: " + id);
        }

        ConsoleUtils.pressEnterToContinue();
    }

    private void updatePet() {
        ConsoleUtils.clearAndShowHeader("Actualizar Mascota");
        petService.updatePet(0); // Pasar 0 para que muestre la lista
        ConsoleUtils.pressEnterToContinue();
    }

    private void deletePet() {
        ConsoleUtils.clearAndShowHeader("Eliminar Mascota");
        petService.deletePet(0); // Pasar 0 para que muestre la lista
        ConsoleUtils.pressEnterToContinue();
    }

    private void listPetsByClient() {
        ConsoleUtils.clearAndShowHeader("Mascotas por Cliente");
        petService.listPetsByClient();
        ConsoleUtils.pressEnterToContinue();
    }
}