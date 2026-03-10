package com.hotelia.servlet;

import com.hotelia.dao.AuditLogDAO;
import com.hotelia.dao.ClientDAO;
import com.hotelia.dao.ReservationDAO;
import com.hotelia.dao.RoomDAO;
import com.hotelia.model.Client;
import com.hotelia.model.Reservation;
import com.hotelia.model.Room;
import com.hotelia.model.User;
import com.hotelia.service.AuditLogService;
import com.hotelia.service.ReservationService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/reservations")
public class ReservationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try (EntityManager em = EntityManagerFactoryProvider.getEMF().createEntityManager()) {
            ReservationDAO reservationDAO = new ReservationDAO(em);
            RoomDAO roomDAO               = new RoomDAO(em);
            ClientDAO clientDAO           = new ClientDAO(em);
            AuditLogDAO auditLogDAO       = new AuditLogDAO(em);
            AuditLogService auditLogService = new AuditLogService(auditLogDAO);
            ReservationService service = new ReservationService(
                    reservationDAO, roomDAO, clientDAO, auditLogService
            );


            List<Reservation> reservations = service.findAll();
            List<Client> clients = clientDAO.findAll();
            List<Room> rooms     = roomDAO.findAll();

            req.setAttribute("reservations", reservations);
            req.setAttribute("clients", clients);
            req.setAttribute("rooms", rooms);
            req.getRequestDispatcher("reservations.jsp").forward(req, resp);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        User user = (User) req.getSession().getAttribute("user");
        String username = user.getUsername();
        String action   = req.getParameter("action");

        try (EntityManager em = EntityManagerFactoryProvider.getEMF().createEntityManager()) {
            ReservationDAO reservationDAO   = new ReservationDAO(em);
            RoomDAO roomDAO                 = new RoomDAO(em);
            ClientDAO clientDAO             = new ClientDAO(em);
            AuditLogDAO auditLogDAO         = new AuditLogDAO(em);
            AuditLogService auditLogService = new AuditLogService(auditLogDAO);
            ReservationService service = new ReservationService(
                    reservationDAO, roomDAO, clientDAO, auditLogService
            );

            if ("create".equals(action)) {
                Long clientId = Long.parseLong(req.getParameter("clientId"));
                Long roomId   = Long.parseLong(req.getParameter("roomId"));
                LocalDate checkIn  = LocalDate.parse(req.getParameter("checkIn"));
                LocalDate checkOut = LocalDate.parse(req.getParameter("checkOut"));
                service.createReservation(clientId, roomId, checkIn, checkOut, username);
                resp.sendRedirect("reservations?success=created");

            } else if ("confirm".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                service.confirm(id, username);
                resp.sendRedirect("reservations?success=confirmed");

            } else if ("cancel".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                service.cancel(id, username);
                resp.sendRedirect("reservations?success=cancelled");

            } else if ("complete".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                service.complete(id, username);
                resp.sendRedirect("reservations?success=completed");

            } else {
                resp.sendRedirect("reservations?error=Action inconnue");
            }

        } catch (Exception e) {
            resp.sendRedirect("reservations?error=" + e.getMessage());
        }
    }
}