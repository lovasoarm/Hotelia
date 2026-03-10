package com.hotelia.dao;

import com.hotelia.model.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ReservationDAO {
    private final EntityManager em;

    public ReservationDAO(EntityManager em) {
        this.em = em;
    }

    public void create(Reservation reservation) {
        em.getTransaction().begin();
        em.persist(reservation);
        em.getTransaction().commit();
    }

    public Reservation find(Long id) {
        return em.find(Reservation.class, id);
    }

    public List<Reservation> findAll() {
        TypedQuery<Reservation> query = em.createQuery("SELECT r FROM Reservation r", Reservation.class);
        return query.getResultList();
    }

    public void update(Reservation reservation) {
        em.getTransaction().begin();
        em.merge(reservation);
        em.getTransaction().commit();
    }

    public void delete(Reservation reservation) {
        em.getTransaction().begin();
        em.remove(em.contains(reservation) ? reservation : em.merge(reservation));
        em.getTransaction().commit();
    }
}