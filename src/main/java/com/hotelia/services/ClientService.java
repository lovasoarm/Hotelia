package com.hotelia.service;

import com.hotelia.dao.ClientDAO;
import com.hotelia.model.Client;
import java.util.List;

public class ClientService {

    private final ClientDAO clientDAO;

    public ClientService(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    public void createClient(Client client) {
        if (client.getFirstName() == null || client.getFirstName().isBlank()) {
            throw new IllegalStateException("Le prénom est obligatoire");
        }
        if (client.getEmail() == null || client.getEmail().isBlank()) {
            throw new IllegalStateException("L'email est obligatoire");
        }
        clientDAO.create(client);
    }

    public Client findById(Long id) {
        Client client = clientDAO.find(id);
        if (client == null) {
            throw new IllegalStateException("Client introuvable");
        }
        return client;
    }

    public List<Client> findAll() {
        return clientDAO.findAll();
    }

    public void updateClient(Client client) {
        if (client == null) {
            throw new IllegalStateException("Client invalide");
        }
        clientDAO.update(client);
    }

    public void deleteClient(Long id) {
        Client client = findById(id);
        if (!client.getReservations().isEmpty()) {
            throw new IllegalStateException(
                    "Impossible : ce client a des réservations"
            );
        }
        clientDAO.delete(client);
    }
}