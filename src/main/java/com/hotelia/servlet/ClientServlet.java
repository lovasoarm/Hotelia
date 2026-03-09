package com.hotelia.servlet;

import com.hotelia.dao.ClientDAO;
import com.hotelia.model.Client;
import com.hotelia.model.User;
import com.hotelia.service.ClientService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/clients")
public class ClientServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        EntityManager em = EntityManagerFactoryProvider.getEMF().createEntityManager();
        try {
            ClientDAO dao = new ClientDAO(em);
            ClientService service = new ClientService(dao);

            List<Client> clients = service.findAll();
            req.setAttribute("clients", clients);
            req.getRequestDispatcher("clients.jsp").forward(req, resp);

        } finally {
            em.close();
        }
    }


    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        EntityManager em = EntityManagerFactoryProvider.getEMF().createEntityManager();
        ClientDAO dao = new ClientDAO(em);
        ClientService service = new ClientService(dao);

        String action = req.getParameter("action");

        try {
            if ("create".equals(action)) {
                String firstName = req.getParameter("firstName");
                String lastName  = req.getParameter("lastName");
                String email     = req.getParameter("email");
                String phone     = req.getParameter("phone");
                String address   = req.getParameter("address");

                Client client = new Client(firstName, lastName, email, phone, address);
                service.createClient(client);
                resp.sendRedirect("clients?success=true");

            } else if ("delete".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                service.deleteClient(id);
                resp.sendRedirect("clients?success=deleted");

            } else {
                resp.sendRedirect("clients?error=Action inconnue");
            }

        } catch (Exception e) {
            resp.sendRedirect("clients?error=" + e.getMessage());
        } finally {
            em.close();
        }
    }
}