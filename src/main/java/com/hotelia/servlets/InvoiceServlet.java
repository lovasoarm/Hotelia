package com.hotelia.servlet;

import com.hotelia.daos.AuditLogDAO;
import com.hotelia.daos.InvoiceDAO;
import com.hotelia.daos.ReservationDAO;
import com.hotelia.models.Invoice;
import com.hotelia.models.User;
import com.hotelia.services.AuditLogService;
import com.hotelia.services.InvoiceService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/invoices")
public class InvoiceServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try (EntityManager em = EntityManagerFactoryProvider.getEMF().createEntityManager()) {
            InvoiceDAO invoiceDAO         = new InvoiceDAO(em);
            ReservationDAO reservationDAO = new ReservationDAO(em);
            AuditLogDAO auditLogDAO       = new AuditLogDAO(em);
            AuditLogService auditLogService = new AuditLogService(auditLogDAO);
            InvoiceService service = new InvoiceService(
                    invoiceDAO, reservationDAO, auditLogService
            );

            List<Invoice> invoices = service.findAll();
            req.setAttribute("invoices", invoices);
            req.getRequestDispatcher("invoices.jsp").forward(req, resp);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        User user = (User) req.getSession().getAttribute("user");
        String username = user.getUsername();
        String action   = req.getParameter("action");

        try (EntityManager em = EntityManagerFactoryProvider.getEMF().createEntityManager()) {
            InvoiceDAO invoiceDAO         = new InvoiceDAO(em);
            ReservationDAO reservationDAO = new ReservationDAO(em);
            AuditLogDAO auditLogDAO       = new AuditLogDAO(em);
            AuditLogService auditLogService = new AuditLogService(auditLogDAO);
            InvoiceService service = new InvoiceService(
                    invoiceDAO, reservationDAO, auditLogService
            );

            if ("generate".equals(action)) {
                Long reservationId = Long.parseLong(req.getParameter("reservationId"));
                service.generateInvoice(reservationId, username);
                resp.sendRedirect("invoices?success=generated");

            } else if ("pay".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                service.markAsPaid(id, username);
                resp.sendRedirect("invoices?success=paid");

            } else {
                resp.sendRedirect("invoices?error=Action inconnue");
            }

        } catch (Exception e) {
            resp.sendRedirect("invoices?error=" + e.getMessage());
        }
    }
}