package com.hotelia.daos;

import com.hotelia.models.Invoice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class InvoiceDAO {
    private final EntityManager em;

    public InvoiceDAO(EntityManager em) {
        this.em = em;
    }

    public void create(Invoice invoice) {
        em.getTransaction().begin();
        em.persist(invoice);
        em.getTransaction().commit();
    }

    public Invoice find(Long id) {
        return em.find(Invoice.class, id);
    }

    public List<Invoice> findAll() {
        TypedQuery<Invoice> query = em.createQuery("SELECT i FROM Invoice i", Invoice.class);
        return query.getResultList();
    }
    public Invoice findByReservation(Long reservationId) {
        TypedQuery<Invoice> query = em.createQuery(
                "SELECT i FROM Invoice i WHERE i.reservation.id = :reservationId",
                Invoice.class
        );
        query.setParameter("reservationId", reservationId);
        List<Invoice> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
    public void update(Invoice invoice) {
        em.getTransaction().begin();
        em.merge(invoice);
        em.getTransaction().commit();
    }

    public void delete(Invoice invoice) {
        em.getTransaction().begin();
        em.remove(em.contains(invoice) ? invoice : em.merge(invoice));
        em.getTransaction().commit();
    }
}