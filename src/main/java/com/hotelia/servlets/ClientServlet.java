package com.hotelia.servlets;

import com.hotelia.daos.AuditLogDAO;
import com.hotelia.daos.ClientDAO;
import com.hotelia.models.Client;
import com.hotelia.models.User;
import com.hotelia.services.AuditLogService;
import com.hotelia.services.ClientService;
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

        try (EntityManager em = EntityManagerFactoryProvider.getEMF().createEntityManager()) {
            ClientDAO dao = new ClientDAO(em);
            AuditLogDAO auditLogDAO = new AuditLogDAO(em);
            AuditLogService auditLogService = new AuditLogService(auditLogDAO);
            ClientService service = new ClientService(dao, auditLogService);

            List<Client> clients = service.findAll();
            req.setAttribute("clients", clients);
            req.getRequestDispatcher("clients.jsp").forward(req, resp);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

      
        User user = (User) req.getSession().getAttribute("user");
        String username = user.getUsername();
        String action = req.getParameter("action");

        try (EntityManager em = EntityManagerFactoryProvider.getEMF().createEntityManager()) {
            ClientDAO dao = new ClientDAO(em);
            AuditLogDAO auditLogDAO = new AuditLogDAO(em);
            AuditLogService auditLogService = new AuditLogService(auditLogDAO);
            ClientService service = new ClientService(dao, auditLogService);

            if ("create".equals(action)) {
                String firstName = req.getParameter("firstName");
                String lastName  = req.getParameter("lastName");
                String email     = req.getParameter("email");
                String phone     = req.getParameter("phone");
                String address   = req.getParameter("address");
                Client client = new Client(firstName, lastName, email, phone, address);
                service.createClient(client, username);
                resp.sendRedirect("clients?success=created");

            } else if ("delete".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                service.deleteClient(id, username);
                resp.sendRedirect("clients?success=deleted");

            } else if ("update".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                Client existing = service.findById(id);
                existing.setFirstName(req.getParameter("firstName"));
                existing.setLastName(req.getParameter("lastName"));
                existing.setEmail(req.getParameter("email"));
                existing.setPhone(req.getParameter("phone"));
                existing.setAddress(req.getParameter("address"));
                service.updateClient(existing, username);
                resp.sendRedirect("clients?success=updated");

            } else {
                resp.sendRedirect("clients?error=Action inconnue");
            }

        } catch (Exception e) {
            resp.sendRedirect("clients?error=" + e.getMessage());
        }
    }
}