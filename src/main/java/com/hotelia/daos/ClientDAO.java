package com.hotelia.dao;

import com.hotelia.model.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ClientDAO {
    private final EntityManager em;

    public ClientDAO(EntityManager em) {
        this.em = em;
    }

    public void create(Client client) {
        em.getTransaction().begin();
        em.persist(client);
        em.getTransaction().commit();
    }

    public Client find(Long id) {
        return em.find(Client.class, id);
    }

    public List<Client> findAll() {
        TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c", Client.class);
        return query.getResultList();
    }

    public void update(Client client) {
        em.getTransaction().begin();
        em.merge(client);
        em.getTransaction().commit();
    }

    public void delete(Client client) {
        em.getTransaction().begin();
        em.remove(em.contains(client) ? client : em.merge(client));
        em.getTransaction().commit();
    }
}