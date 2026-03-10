package com.hotelia.daos;

import com.hotelia.models.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class RoomDAO {
    private final EntityManager em;

    public RoomDAO(EntityManager em) {
        this.em = em;
    }

    public void create(Room room) {
        em.getTransaction().begin();
        em.persist(room);
        em.getTransaction().commit();
    }

    public Room find(Long id) {
        return em.find(Room.class, id);
    }

    public List<Room> findAll() {
        TypedQuery<Room> query = em.createQuery("SELECT r FROM Room r", Room.class);
        return query.getResultList();
    }

    public void update(Room room) {
        em.getTransaction().begin();
        em.merge(room);
        em.getTransaction().commit();
    }

    public void delete(Room room) {
        em.getTransaction().begin();
        em.remove(em.contains(room) ? room : em.merge(room));
        em.getTransaction().commit();
    }
}