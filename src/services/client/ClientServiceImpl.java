package services.client;

import models.Client;
import services.client.ClientService;
import config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientServiceImpl implements ClientService {
    private static List<Client> clients = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    // Carga clientes desde la base de datos al ArrayList (cache)
    private void loadClientsFromDB() {
        clients.clear();
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM client";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("last_name"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
                clients.add(client);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar clientes: " + e.getMessage());
        }
    }

    @Override
    public void createClient() {
        System.out.print("Nombre: ");
        String name = scanner.nextLine();
        System.out.print("Apellido: ");
        String lastName = scanner.nextLine();
        System.out.print("Teléfono: ");
        String phone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "INSERT INTO client (name, last_name, phone, email) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, name);
            stmt.setString(2, lastName);
            stmt.setString(3, phone);
            stmt.setString(4, email);

            stmt.executeUpdate();

            // Obtener el ID generado y actualizar el cache
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                Client client = new Client(name, lastName, phone, email);
                client.setId(rs.getInt(1));
                clients.add(client);
            }

            System.out.println("Cliente guardado exitosamente.");

        } catch (SQLException e) {
            System.err.println("Error al guardar cliente: " + e.getMessage());
        }
    }

    @Override
    public List<Client> readAllClients() {
        loadClientsFromDB(); // Actualizar cache desde BD
        return clients;
    }

    @Override
    public Client findClientById(int id) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM client WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Client client = new Client(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("last_name"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
                return client;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void updateClient(int id) {
        // Mostrar clientes disponibles si no se proporciona ID
        if (id == 0) {
            List<Client> allClients = readAllClients();
            if (allClients.isEmpty()) {
                System.out.println("❌ No hay clientes registrados.");
                return;
            }

            System.out.println("--- Clientes Disponibles ---");
            for (Client c : allClients) {
                System.out.printf("ID: %d - %s %s (%s)%n",
                        c.getId(), c.getName(), c.getLastName(), c.getPhone());
            }
            System.out.println();

            System.out.print("ID del cliente a actualizar: ");
            id = scanner.nextInt();
            scanner.nextLine();
        }

        Client existingClient = findClientById(id);
        if (existingClient == null) {
            System.out.println("❌ Cliente no encontrado con ID: " + id);
            return;
        }

        System.out.println("✔ Cliente encontrado: " + existingClient);
        System.out.println("Ingresa los nuevos datos del cliente:");

        System.out.print("Nombre: ");
        String name = scanner.nextLine();
        System.out.print("Apellido: ");
        String lastName = scanner.nextLine();
        System.out.print("Teléfono: ");
        String phone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "UPDATE client SET name = ?, last_name = ?, phone = ?, email = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, name);
            stmt.setString(2, lastName);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setInt(5, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✔ Cliente actualizado exitosamente.");
                loadClientsFromDB(); // Actualizar cache
            } else {
                System.out.println("❌ No se encontró el cliente con ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
        }
    }

    @Override
    public void deleteClient(int id) {
        // Mostrar clientes disponibles si no se proporciona ID
        if (id == 0) {
            List<Client> allClients = readAllClients();
            if (allClients.isEmpty()) {
                System.out.println("❌ No hay clientes registrados.");
                return;
            }

            System.out.println("--- Clientes Disponibles ---");
            for (Client c : allClients) {
                System.out.printf("ID: %d - %s %s (%s)%n",
                        c.getId(), c.getName(), c.getLastName(), c.getPhone());
            }
            System.out.println();

            System.out.print("ID del cliente a eliminar: ");
            id = scanner.nextInt();
            scanner.nextLine();
        }

        Client existingClient = findClientById(id);
        if (existingClient == null) {
            System.out.println("❌ Cliente no encontrado con ID: " + id);
            return;
        }

        System.out.println("⚠️  Cliente a eliminar: " + existingClient);
        System.out.print("¿Está seguro que desea eliminarlo? (s/n): ");
        String confirm = scanner.nextLine();

        if (confirm.toLowerCase().equals("s") || confirm.toLowerCase().equals("si")) {
            try (Connection conn = DatabaseConfig.getConnection()) {
                String sql = "DELETE FROM client WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("✔ Cliente eliminado exitosamente.");
                    loadClientsFromDB(); // Actualizar cache
                } else {
                    System.out.println("❌ No se encontró el cliente con ID: " + id);
                }

            } catch (SQLException e) {
                System.err.println("Error al eliminar cliente: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Operación cancelada.");
        }
    }
}