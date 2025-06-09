package services.client;

import models.Client;
import services.client.ClientService;
import java.util.ArrayList;
import java.util.List;


public class ClientServiceImpl implements ClientService {
    private static List<Client> clients = new ArrayList<>();

    @Override
    public void saveClient(Client client) {
        clients.add(client);
    }

    @Override
    public List<Client> listClients() {
        return clients;
    }

    @Override
    public Client findClientById(int id) {
        for (Client client : clients) {
            if (client.getId() == id) {
                return client;
            }
        }
        return null;
    }
}
