package com.hotelia.service;

import com.hotelia.dao.UserDAO;
import com.hotelia.model.User;

public class AuthService {

    private UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User login(String username, String password) {

        User user = userDAO.findByUsername(username);

        if(user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }
}