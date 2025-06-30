package services.client;

import models.Client;
import java.util.List;

public interface ClientService {
    void createClient();
    List<Client> readAllClients();
    Client findClientById(int id);
    void updateClient(int id);
    void deleteClient(int id);
}