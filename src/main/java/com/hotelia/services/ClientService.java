package com.hotelia.services;

import com.hotelia.daos.ClientDAO;
import com.hotelia.models.Client;
import java.util.List;

public class ClientService {

    private final ClientDAO clientDAO;
    private final AuditLogService auditLogService;


    public ClientService(ClientDAO clientDAO, AuditLogService auditLogService) {
        this.clientDAO       = clientDAO;
        this.auditLogService = auditLogService;
    }

    public void createClient(Client client, String username) {
        if (client.getFirstName() == null || client.getFirstName().isBlank()) {
            throw new IllegalStateException("Le prénom est obligatoire");
        }
        if (client.getEmail() == null || client.getEmail().isBlank()) {
            throw new IllegalStateException("L'email est obligatoire");
        }
        clientDAO.create(client);
        auditLogService.log("CREATE_CLIENT", username,
                "Client " + client.getFullName() + " créé");
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

    public void updateClient(Client client, String username) {
        if (client == null) {
            throw new IllegalStateException("Client invalide");
        }
        clientDAO.update(client);
        auditLogService.log("UPDATE_CLIENT", username,
                "Client #" + client.getId() + " mis à jour");
    }

    public void deleteClient(Long id, String username) {
        Client client = findById(id);
        if (!client.getReservations().isEmpty()) {
            throw new IllegalStateException(
                    "Impossible : ce client a des réservations");
        }
        clientDAO.delete(client);
        auditLogService.log("DELETE_CLIENT", username,
                "Client " + client.getFullName() + " supprimé");
    }
}