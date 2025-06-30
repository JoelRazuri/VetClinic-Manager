package services.pet;

import services.pet.PetService;
import models.Pet;
import models.Client;
import services.client.ClientService;
import services.client.ClientServiceImpl;
import config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PetServiceImpl implements PetService {
    private static List<Pet> pets = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    // Carga mascotas desde la base de datos al ArrayList (cache)
    private void loadPetsFromDB() {
        pets.clear();
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM pet";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pet pet = new Pet(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("species"),
                        rs.getString("race"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getInt("id_client")
                );
                pets.add(pet);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar mascotas: " + e.getMessage());
        }
    }

    @Override
    public void createPet() {
        // Mostrar clientes disponibles
        ClientService clientService = new ClientServiceImpl();
        List<Client> clients = clientService.readAllClients();

        if (clients.isEmpty()) {
            System.out.println("❌ No hay clientes registrados. Debe crear un cliente primero.");
            return;
        }

        System.out.println("--- Clientes Disponibles ---");
        for (Client c : clients) {
            System.out.printf("ID: %d - %s %s%n", c.getId(), c.getName(), c.getLastName());
        }
        System.out.println();

        System.out.print("ID del cliente dueño: ");
        int idClient = scanner.nextInt();
        scanner.nextLine();

        // Validar que el cliente existe
        Client client = clientService.findClientById(idClient);
        if (client == null) {
            System.out.println("❌ Cliente no encontrado con ID: " + idClient);
            return;
        }

        System.out.print("Nombre de la mascota: ");
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

        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "INSERT INTO pet (name, species, race, gender, age, id_client) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, name);
            stmt.setString(2, species);
            stmt.setString(3, race);
            stmt.setString(4, gender);
            stmt.setInt(5, age);
            stmt.setInt(6, idClient);

            stmt.executeUpdate();

            // Obtener el ID generado y actualizar el cache
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                Pet pet = new Pet(name, species, race, gender, age, idClient);
                pet.setId(rs.getInt(1));
                pets.add(pet);
            }

            System.out.println("✔ Mascota guardada exitosamente para " + client.getName() + " " + client.getLastName());

        } catch (SQLException e) {
            System.err.println("Error al guardar mascota: " + e.getMessage());
        }
    }

    @Override
    public List<Pet> readAllPets() {
        loadPetsFromDB(); // Actualizar cache desde BD
        return pets;
    }

    @Override
    public Pet findPetById(int id) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM pet WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Pet pet = new Pet(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("species"),
                        rs.getString("race"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getInt("id_client")
                );
                return pet;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar mascota: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void updatePet(int id) {
        // Mostrar mascotas disponibles si no se proporciona ID
        if (id == 0) {
            List<Pet> allPets = readAllPets();
            if (allPets.isEmpty()) {
                System.out.println("❌ No hay mascotas registradas.");
                return;
            }

            System.out.println("--- Mascotas Disponibles ---");
            for (Pet p : allPets) {
                System.out.printf("ID: %d - %s (%s) - Cliente ID: %d%n",
                        p.getId(), p.getName(), p.getSpecies(), p.getIdClient());
            }
            System.out.println();

            System.out.print("ID de la mascota a actualizar: ");
            id = scanner.nextInt();
            scanner.nextLine();
        }

        Pet existingPet = findPetById(id);
        if (existingPet == null) {
            System.out.println("❌ Mascota no encontrada con ID: " + id);
            return;
        }

        System.out.println("✔ Mascota encontrada: " + existingPet);
        System.out.println("Ingresa los nuevos datos de la mascota:");

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

        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "UPDATE pet SET name = ?, species = ?, race = ?, gender = ?, age = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, name);
            stmt.setString(2, species);
            stmt.setString(3, race);
            stmt.setString(4, gender);
            stmt.setInt(5, age);
            stmt.setInt(6, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✔ Mascota actualizada exitosamente.");
                loadPetsFromDB(); // Actualizar cache
            } else {
                System.out.println("❌ No se encontró la mascota con ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar mascota: " + e.getMessage());
        }
    }

    @Override
    public void deletePet(int id) {
        // Mostrar mascotas disponibles si no se proporciona ID
        if (id == 0) {
            List<Pet> allPets = readAllPets();
            if (allPets.isEmpty()) {
                System.out.println("❌ No hay mascotas registradas.");
                return;
            }

            System.out.println("--- Mascotas Disponibles ---");
            for (Pet p : allPets) {
                System.out.printf("ID: %d - %s (%s) - Cliente ID: %d%n",
                        p.getId(), p.getName(), p.getSpecies(), p.getIdClient());
            }
            System.out.println();

            System.out.print("ID de la mascota a eliminar: ");
            id = scanner.nextInt();
            scanner.nextLine();
        }

        Pet existingPet = findPetById(id);
        if (existingPet == null) {
            System.out.println("❌ Mascota no encontrada con ID: " + id);
            return;
        }

        System.out.println("⚠️  Mascota a eliminar: " + existingPet);
        System.out.print("¿Está seguro que desea eliminarla? (s/n): ");
        String confirm = scanner.nextLine();

        if (confirm.toLowerCase().equals("s") || confirm.toLowerCase().equals("si")) {
            try (Connection conn = DatabaseConfig.getConnection()) {
                String sql = "DELETE FROM pet WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("✔ Mascota eliminada exitosamente.");
                    loadPetsFromDB(); // Actualizar cache
                } else {
                    System.out.println("❌ No se encontró la mascota con ID: " + id);
                }

            } catch (SQLException e) {
                System.err.println("Error al eliminar mascota: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Operación cancelada.");
        }
    }

    @Override
    public void listPetsByClient() {
        // Mostrar clientes disponibles
        ClientService clientService = new ClientServiceImpl();
        List<Client> clients = clientService.readAllClients();

        if (clients.isEmpty()) {
            System.out.println("❌ No hay clientes registrados.");
            return;
        }

        System.out.println("--- Clientes Disponibles ---");
        for (Client c : clients) {
            System.out.printf("ID: %d - %s %s%n", c.getId(), c.getName(), c.getLastName());
        }
        System.out.println();

        System.out.print("ID del cliente: ");
        int clientId = scanner.nextInt();
        scanner.nextLine();

        // Validar que el cliente existe
        Client client = clientService.findClientById(clientId);
        if (client == null) {
            System.out.println("❌ Cliente no encontrado con ID: " + clientId);
            return;
        }

        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM pet WHERE id_client = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            List<Pet> clientPets = new ArrayList<>();
            while (rs.next()) {
                Pet pet = new Pet(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("species"),
                        rs.getString("race"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getInt("id_client")
                );
                clientPets.add(pet);
            }

            System.out.println("\n✔ Cliente: " + client.getName() + " " + client.getLastName());

            if (clientPets.isEmpty()) {
                System.out.println("❌ No se encontraron mascotas para este cliente.");
            } else {
                System.out.println("\n--- Mascotas del Cliente ---");
                System.out.printf("%-5s | %-12s | %-10s | %-12s | %-9s | %-5s%n",
                        "ID", "Nombre", "Especie", "Raza", "Género", "Edad");
                System.out.println("-".repeat(60));

                for (Pet pet : clientPets) {
                    System.out.printf("%-5d | %-12s | %-10s | %-12s | %-9s | %-5d%n",
                            pet.getId(), pet.getName(), pet.getSpecies(),
                            pet.getRace(), pet.getGender(), pet.getAge());
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar mascotas del cliente: " + e.getMessage());
        }
    }
}