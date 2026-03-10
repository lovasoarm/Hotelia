package com.hotelia.servlets;

import com.hotelia.daos.UserDAO;
import com.hotelia.enums.Role;
import com.hotelia.models.User;
import com.hotelia.services.AuthService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        EntityManager em = EntityManagerFactoryProvider.getEMF().createEntityManager();

        UserDAO userDAO = new UserDAO(em);
        AuthService authService = new AuthService(userDAO);

        User user = authService.login(username, password);

        if (user != null) {

            request.getSession().setAttribute("user", user);

            if (user.getRole() == Role.ADMIN) {
                response.sendRedirect("admin.jsp");
            } else if (user.getRole() == Role.RECEPTIONIST) {
                response.sendRedirect("receptionist.jsp");
            }

        } else {
            response.sendRedirect("login.jsp?error=true");
        }
    }
}