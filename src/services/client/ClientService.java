package services.client;

import models.Client;
import java.util.List;


public interface ClientService {
    void saveClient(Client client);
    List<Client> listClients();
    Client findClientById(int id);
}
