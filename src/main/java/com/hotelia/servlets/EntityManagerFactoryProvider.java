package com.hotelia.servlet;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerFactoryProvider {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("hotelia");

    public static EntityManagerFactory getEMF() {
        return emf;
    }
}