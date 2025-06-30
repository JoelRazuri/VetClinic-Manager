package services.medicalhistory;

import services.medicalhistory.MedicalHistoryService;
import models.MedicalHistory;
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

public class MedicalHistoryServiceImpl implements MedicalHistoryService {
    private static List<MedicalHistory> medicalHistories = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    private void loadMedicalHistoriesFromDB() {
        medicalHistories.clear();
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM medical_history";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MedicalHistory medicalHistory = new MedicalHistory(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getString("description"),
                        rs.getString("treatment"),
                        rs.getString("vaccines"),
                        rs.getInt("id_pet")
                );
                medicalHistories.add(medicalHistory);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar historiales médicos: " + e.getMessage());
        }
    }

    @Override
    public void createMedicalHistory() {
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

        Pet pet = petService.findPetById(idPet);
        if (pet == null) {
            System.out.println("❌ Mascota no encontrada con ID: " + idPet);
            return;
        }

        ClientService clientService = new ClientServiceImpl();
        Client client = clientService.findClientById(pet.getIdClient());
        if (client == null) {
            System.out.println("❌ Cliente no encontrado para la mascota.");
            return;
        }

        System.out.println("✔ Mascota: " + pet.getName() + " (" + pet.getSpecies() + ")");
        System.out.println("✔ Cliente: " + client.getName() + " " + client.getLastName());
        System.out.println();

        System.out.print("Fecha (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        Date date;
        try {
            date = Date.valueOf(dateStr);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Formato de fecha inválido. Use YYYY-MM-DD");
            return;
        }

        System.out.print("Descripción: ");
        String description = scanner.nextLine();

        System.out.print("Tratamiento: ");
        String treatment = scanner.nextLine();

        System.out.print("Vacunas (opcional): ");
        String vaccines = scanner.nextLine();
        if (vaccines.trim().isEmpty()) {
            vaccines = null;
        }

        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "INSERT INTO medical_history (date, description, treatment, vaccines, id_pet) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setDate(1, date);
            stmt.setString(2, description);
            stmt.setString(3, treatment);
            stmt.setString(4, vaccines);
            stmt.setInt(5, idPet);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                MedicalHistory medicalHistory = new MedicalHistory(date, description, treatment, vaccines, idPet);
                medicalHistory.setId(rs.getInt(1));
                medicalHistories.add(medicalHistory);
            }

            System.out.println("✔ Historial médico creado exitosamente para " + pet.getName() +
                    " (Cliente: " + client.getName() + " " + client.getLastName() + ")");

        } catch (SQLException e) {
            System.err.println("Error al crear historial médico: " + e.getMessage());
        }
    }

    @Override
    public List<MedicalHistory> readAllMedicalHistories() {
        loadMedicalHistoriesFromDB();
        return medicalHistories;
    }

    @Override
    public MedicalHistory readMedicalHistory(int id) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM medical_history WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                MedicalHistory medicalHistory = new MedicalHistory(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getString("description"),
                        rs.getString("treatment"),
                        rs.getString("vaccines"),
                        rs.getInt("id_pet")
                );
                return medicalHistory;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar historial médico: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void updateMedicalHistory(int id) {
        if (id == 0) {
            List<MedicalHistory> allMedicalHistories = readAllMedicalHistories();
            if (allMedicalHistories.isEmpty()) {
                System.out.println("❌ No hay historiales médicos registrados.");
                return;
            }

            System.out.println("--- Historiales Médicos Disponibles ---");
            for (MedicalHistory mh : allMedicalHistories) {
                System.out.printf("ID: %d - %s | %s | Pet ID: %d%n",
                        mh.getId(), mh.getDate(), mh.getDescription(), mh.getIdPet());
            }
            System.out.println();

            System.out.print("ID del historial médico a actualizar: ");
            id = scanner.nextInt();
            scanner.nextLine();
        }

        MedicalHistory existingMedicalHistory = readMedicalHistory(id);
        if (existingMedicalHistory == null) {
            System.out.println("❌ Historial médico no encontrado con ID: " + id);
            return;
        }

        System.out.println("✔ Historial médico encontrado: " + existingMedicalHistory);
        System.out.println("Ingresa los nuevos datos del historial médico:");

        System.out.print("Fecha (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        Date date;
        try {
            date = Date.valueOf(dateStr);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Formato de fecha inválido. Use YYYY-MM-DD");
            return;
        }

        System.out.print("Descripción: ");
        String description = scanner.nextLine();

        System.out.print("Tratamiento: ");
        String treatment = scanner.nextLine();

        System.out.print("Vacunas (opcional): ");
        String vaccines = scanner.nextLine();
        if (vaccines.trim().isEmpty()) {
            vaccines = null;
        }

        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "UPDATE medical_history SET date = ?, description = ?, treatment = ?, vaccines = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setDate(1, date);
            stmt.setString(2, description);
            stmt.setString(3, treatment);
            stmt.setString(4, vaccines);
            stmt.setInt(5, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✔ Historial médico actualizado exitosamente.");
                loadMedicalHistoriesFromDB();
            } else {
                System.out.println("❌ No se encontró el historial médico con ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar historial médico: " + e.getMessage());
        }
    }

    @Override
    public void deleteMedicalHistory(int id) {
        if (id == 0) {
            List<MedicalHistory> allMedicalHistories = readAllMedicalHistories();
            if (allMedicalHistories.isEmpty()) {
                System.out.println("❌ No hay historiales médicos registrados.");
                return;
            }

            System.out.println("--- Historiales Médicos Disponibles ---");
            for (MedicalHistory mh : allMedicalHistories) {
                System.out.printf("ID: %d - %s | %s | Pet ID: %d%n",
                        mh.getId(), mh.getDate(), mh.getDescription(), mh.getIdPet());
            }
            System.out.println();

            System.out.print("ID del historial médico a eliminar: ");
            id = scanner.nextInt();
            scanner.nextLine();
        }

        MedicalHistory existingMedicalHistory = readMedicalHistory(id);
        if (existingMedicalHistory == null) {
            System.out.println("❌ Historial médico no encontrado con ID: " + id);
            return;
        }

        System.out.println("⚠️  Historial médico a eliminar: " + existingMedicalHistory);
        System.out.print("¿Está seguro que desea eliminarlo? (s/n): ");
        String confirm = scanner.nextLine();

        if (confirm.toLowerCase().equals("s") || confirm.toLowerCase().equals("si")) {
            try (Connection conn = DatabaseConfig.getConnection()) {
                String sql = "DELETE FROM medical_history WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("✔ Historial médico eliminado exitosamente.");
                    loadMedicalHistoriesFromDB();
                } else {
                    System.out.println("❌ No se encontró el historial médico con ID: " + id);
                }

            } catch (SQLException e) {
                System.err.println("Error al eliminar historial médico: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Operación cancelada.");
        }
    }

    @Override
    public List<MedicalHistory> findHistoriesByPet(int idPet) {
        PetService petService = new PetServiceImpl();
        List<Pet> pets = petService.readAllPets();

        if (pets.isEmpty()) {
            System.out.println("❌ No hay mascotas registradas.");
            return new ArrayList<>();
        }

        System.out.println("--- Mascotas Disponibles ---");
        for (Pet p : pets) {
            System.out.printf("ID: %d - %s (%s) - Cliente ID: %d%n",
                    p.getId(), p.getName(), p.getSpecies(), p.getIdClient());
        }
        System.out.println();

        System.out.print("ID de la mascota: ");
        idPet = scanner.nextInt();
        scanner.nextLine();

        Pet pet = petService.findPetById(idPet);
        if (pet == null) {
            System.out.println("❌ Mascota no encontrada con ID: " + idPet);
            return new ArrayList<>();
        }

        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM medical_history WHERE id_pet = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idPet);
            ResultSet rs = stmt.executeQuery();

            List<MedicalHistory> petMedicalHistories = new ArrayList<>();
            while (rs.next()) {
                MedicalHistory medicalHistory = new MedicalHistory(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getString("description"),
                        rs.getString("treatment"),
                        rs.getString("vaccines"),
                        rs.getInt("id_pet")
                );
                petMedicalHistories.add(medicalHistory);
            }

            System.out.println("\n✔ Mascota: " + pet.getName() + " (" + pet.getSpecies() + ")");

            if (petMedicalHistories.isEmpty()) {
                System.out.println("❌ No se encontraron historiales médicos para esta mascota.");
            } else {
                System.out.println("\n--- Historiales Médicos de la Mascota ---");
                System.out.printf("%-5s | %-12s | %-15s | %-15s | %-15s%n",
                        "ID", "Fecha", "Descripción", "Tratamiento", "Vacunas");
                System.out.println("-".repeat(80));

                for (MedicalHistory mh : petMedicalHistories) {
                    System.out.printf("%-5d | %-12s | %-15s | %-15s | %-15s%n",
                            mh.getId(), mh.getDate(), mh.getDescription(), mh.getTreatment(),
                            (mh.getVaccines() != null && !mh.getVaccines().isEmpty()) ? mh.getVaccines() : "N/A");
                }
            }

            return petMedicalHistories;

        } catch (SQLException e) {
            System.err.println("Error al buscar historiales médicos de la mascota: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}