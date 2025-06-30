package services.medicalshift;

import services.medicalshift.MedicalShiftService;
import models.MedicalShift;
import models.Pet;
import models.Client;
import services.pet.PetService;
import services.pet.PetServiceImpl;
import services.client.ClientService;
import services.client.ClientServiceImpl;
import config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MedicalShiftServiceImpl implements MedicalShiftService {
    private static List<MedicalShift> medicalShifts = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    // Carga citas médicas desde la base de datos al ArrayList (cache)
    private void loadMedicalShiftsFromDB() {
        medicalShifts.clear();
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM medical_shift";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MedicalShift medicalShift = new MedicalShift(
                        rs.getInt("id"),
                        rs.getString("reason"),
                        rs.getDate("date"),
                        rs.getDouble("price"),
                        rs.getString("status"),
                        rs.getInt("id_pet"),
                        rs.getInt("id_client")
                );
                medicalShifts.add(medicalShift);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar citas médicas: " + e.getMessage());
        }
    }

    @Override
    public void createMedicalShift() {
        // Mostrar mascotas disponibles
        PetService petService = new PetServiceImpl();
        List<Pet> pets = petService.readAllPets();

        if (pets.isEmpty()) {
            System.out.println("❌ No hay mascotas registradas. Debe registrar una mascota primero.");
            return;
        }

        System.out.println("--- Mascotas Disponibles ---");
        for (Pet p : pets) {
            System.out.printf("ID: %d - %s (%s) - Cliente ID: %d%n",
                    p.getId(), p.getName(), p.getSpecies(), p.getIdClient());
        }
        System.out.println();

        System.out.print("ID de la mascota: ");
        int idPet = scanner.nextInt();
        scanner.nextLine();

        // Validar que la mascota existe
        Pet pet = petService.findPetById(idPet);
        if (pet == null) {
            System.out.println("❌ Mascota no encontrada con ID: " + idPet);
            return;
        }

        // Obtener el cliente de la mascota
        ClientService clientService = new ClientServiceImpl();
        Client client = clientService.findClientById(pet.getIdClient());
        if (client == null) {
            System.out.println("❌ Cliente no encontrado para la mascota.");
            return;
        }

        System.out.println("✔ Mascota: " + pet.getName() + " (" + pet.getSpecies() + ")");
        System.out.println("✔ Cliente: " + client.getName() + " " + client.getLastName());
        System.out.println();

        System.out.print("Motivo de la consulta: ");
        String reason = scanner.nextLine();

        System.out.print("Fecha (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        Date date;
        try {
            date = Date.valueOf(dateStr);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Formato de fecha inválido. Use YYYY-MM-DD");
            return;
        }

        System.out.print("Precio de la consulta: $");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Estado (Programada/Completada/Cancelada): ");
        String status = scanner.nextLine();

        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "INSERT INTO medical_shift (reason, date, price, status, id_pet, id_client) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, reason);
            stmt.setDate(2, date);
            stmt.setDouble(3, price);
            stmt.setString(4, status);
            stmt.setInt(5, idPet);
            stmt.setInt(6, pet.getIdClient());

            stmt.executeUpdate();

            // Obtener el ID generado y actualizar el cache
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                MedicalShift medicalShift = new MedicalShift(reason, date, price, status, idPet, pet.getIdClient());
                medicalShift.setId(rs.getInt(1));
                medicalShifts.add(medicalShift);
            }

            System.out.println("✔ Cita médica creada exitosamente para " + pet.getName() +
                    " (Cliente: " + client.getName() + " " + client.getLastName() + ")");

        } catch (SQLException e) {
            System.err.println("Error al crear cita médica: " + e.getMessage());
        }
    }

    @Override
    public List<MedicalShift> readAllMedicalShift() {
        loadMedicalShiftsFromDB(); // Actualizar cache desde BD
        return medicalShifts;
    }

    @Override
    public MedicalShift readMedicalShift(int id) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM medical_shift WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                MedicalShift medicalShift = new MedicalShift(
                        rs.getInt("id"),
                        rs.getString("reason"),
                        rs.getDate("date"),
                        rs.getDouble("price"),
                        rs.getString("status"),
                        rs.getInt("id_pet"),
                        rs.getInt("id_client")
                );
                return medicalShift;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cita médica: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void updateMedicalShift(int id) {
        // Mostrar citas médicas disponibles si no se proporciona ID
        if (id == 0) {
            List<MedicalShift> allMedicalShifts = readAllMedicalShift();
            if (allMedicalShifts.isEmpty()) {
                System.out.println("❌ No hay citas médicas registradas.");
                return;
            }

            System.out.println("--- Citas Médicas Disponibles ---");
            for (MedicalShift ms : allMedicalShifts) {
                System.out.printf("ID: %d - %s | %s | $%.2f | %s%n",
                        ms.getId(), ms.getReason(), ms.getDate(), ms.getPrice(), ms.getStatus());
            }
            System.out.println();

            System.out.print("ID de la cita médica a actualizar: ");
            id = scanner.nextInt();
            scanner.nextLine();
        }

        MedicalShift existingMedicalShift = readMedicalShift(id);
        if (existingMedicalShift == null) {
            System.out.println("❌ Cita médica no encontrada con ID: " + id);
            return;
        }

        System.out.println("✔ Cita médica encontrada: " + existingMedicalShift);
        System.out.println("Ingresa los nuevos datos de la cita médica:");

        System.out.print("Motivo de la consulta: ");
        String reason = scanner.nextLine();

        System.out.print("Fecha (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        Date date;
        try {
            date = Date.valueOf(dateStr);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Formato de fecha inválido. Use YYYY-MM-DD");
            return;
        }

        System.out.print("Precio de la consulta: $");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Estado (Programada/Completada/Cancelada): ");
        String status = scanner.nextLine();

        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "UPDATE medical_shift SET reason = ?, date = ?, price = ?, status = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, reason);
            stmt.setDate(2, date);
            stmt.setDouble(3, price);
            stmt.setString(4, status);
            stmt.setInt(5, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✔ Cita médica actualizada exitosamente.");
                loadMedicalShiftsFromDB(); // Actualizar cache
            } else {
                System.out.println("❌ No se encontró la cita médica con ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar cita médica: " + e.getMessage());
        }
    }

    @Override
    public void deleteMedicalShift(int id) {
        // Mostrar citas médicas disponibles si no se proporciona ID
        if (id == 0) {
            List<MedicalShift> allMedicalShifts = readAllMedicalShift();
            if (allMedicalShifts.isEmpty()) {
                System.out.println("❌ No hay citas médicas registradas.");
                return;
            }

            System.out.println("--- Citas Médicas Disponibles ---");
            for (MedicalShift ms : allMedicalShifts) {
                System.out.printf("ID: %d - %s | %s | $%.2f | %s%n",
                        ms.getId(), ms.getReason(), ms.getDate(), ms.getPrice(), ms.getStatus());
            }
            System.out.println();

            System.out.print("ID de la cita médica a eliminar: ");
            id = scanner.nextInt();
            scanner.nextLine();
        }

        MedicalShift existingMedicalShift = readMedicalShift(id);
        if (existingMedicalShift == null) {
            System.out.println("❌ Cita médica no encontrada con ID: " + id);
            return;
        }

        System.out.println("⚠️  Cita médica a eliminar: " + existingMedicalShift);
        System.out.print("¿Está seguro que desea eliminarla? (s/n): ");
        String confirm = scanner.nextLine();

        if (confirm.toLowerCase().equals("s") || confirm.toLowerCase().equals("si")) {
            try (Connection conn = DatabaseConfig.getConnection()) {
                String sql = "DELETE FROM medical_shift WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("✔ Cita médica eliminada exitosamente.");
                    loadMedicalShiftsFromDB(); // Actualizar cache
                } else {
                    System.out.println("❌ No se encontró la cita médica con ID: " + id);
                }

            } catch (SQLException e) {
                System.err.println("Error al eliminar cita médica: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Operación cancelada.");
        }
    }

    @Override
    public List<MedicalShift> getMedicalShiftByClient(int idClient) {
        // Mostrar clientes disponibles
        ClientService clientService = new ClientServiceImpl();
        List<Client> clients = clientService.readAllClients();

        if (clients.isEmpty()) {
            System.out.println("❌ No hay clientes registrados.");
            return new ArrayList<>();
        }

        System.out.println("--- Clientes Disponibles ---");
        for (Client c : clients) {
            System.out.printf("ID: %d - %s %s%n", c.getId(), c.getName(), c.getLastName());
        }
        System.out.println();

        System.out.print("ID del cliente: ");
        idClient = scanner.nextInt();
        scanner.nextLine();

        // Validar que el cliente existe
        Client client = clientService.findClientById(idClient);
        if (client == null) {
            System.out.println("❌ Cliente no encontrado con ID: " + idClient);
            return new ArrayList<>();
        }

        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM medical_shift WHERE id_client = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();

            List<MedicalShift> clientMedicalShifts = new ArrayList<>();
            while (rs.next()) {
                MedicalShift medicalShift = new MedicalShift(
                        rs.getInt("id"),
                        rs.getString("reason"),
                        rs.getDate("date"),
                        rs.getDouble("price"),
                        rs.getString("status"),
                        rs.getInt("id_pet"),
                        rs.getInt("id_client")
                );
                clientMedicalShifts.add(medicalShift);
            }

            System.out.println("\n✔ Cliente: " + client.getName() + " " + client.getLastName());

            if (clientMedicalShifts.isEmpty()) {
                System.out.println("❌ No se encontraron citas médicas para este cliente.");
            } else {
                System.out.println("\n--- Citas Médicas del Cliente ---");
                System.out.printf("%-5s | %-15s | %-12s | %-10s | %-12s | %-8s%n",
                        "ID", "Motivo", "Fecha", "Precio", "Estado", "Pet ID");
                System.out.println("-".repeat(80));

                for (MedicalShift ms : clientMedicalShifts) {
                    System.out.printf("%-5d | %-15s | %-12s | $%-9.2f | %-12s | %-8d%n",
                            ms.getId(), ms.getReason(), ms.getDate(), ms.getPrice(),
                            ms.getStatus(), ms.getIdPet());
                }
            }

            return clientMedicalShifts;

        } catch (SQLException e) {
            System.err.println("Error al buscar citas médicas del cliente: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}