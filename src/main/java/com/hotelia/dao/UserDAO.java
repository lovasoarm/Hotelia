package com.hotelia.dao;

import com.hotelia.model.User;
import jakarta.persistence.*;

public class UserDAO {

    private final EntityManager em;

    public UserDAO(EntityManager em) {
        this.em = em;
    }

    public User findByUsername(String username) {
        return em.createQuery(
                        "SELECT u FROM User u WHERE u.username = :username",
                        User.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public void save(User user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }
}