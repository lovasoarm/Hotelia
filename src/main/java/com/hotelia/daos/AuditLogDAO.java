package com.hotelia.dao;

import com.hotelia.model.AuditLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class AuditLogDAO {

    private final EntityManager em;

    public AuditLogDAO(EntityManager em) {
        this.em = em;
    }

    public void save(AuditLog log) {
        em.getTransaction().begin();
        em.persist(log);
        em.getTransaction().commit();
    }

    public List<AuditLog> findAll() {
        TypedQuery<AuditLog> query = em.createQuery(
                "SELECT a FROM AuditLog a ORDER BY a.date DESC", AuditLog.class
        );
        return query.getResultList();
    }
}