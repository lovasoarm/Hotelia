package com.hotelia.servlets;

import com.hotelia.daos.AuditLogDAO;
import com.hotelia.daos.RoomDAO;
import com.hotelia.enums.RoomStatus;
import com.hotelia.enums.RoomType;
import com.hotelia.models.Room;
import com.hotelia.models.User;
import com.hotelia.services.AuditLogService;
import com.hotelia.services.RoomService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/rooms")
public class RoomServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try (EntityManager em = EntityManagerFactoryProvider.getEMF().createEntityManager()) {
            RoomDAO dao = new RoomDAO(em);
            AuditLogDAO auditLogDAO = new AuditLogDAO(em);
            AuditLogService auditLogService = new AuditLogService(auditLogDAO);
            RoomService service = new RoomService(dao, auditLogService);

            List<Room> rooms = service.findAll();
            req.setAttribute("rooms", rooms);
            req.getRequestDispatcher("rooms.jsp").forward(req, resp);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        User user = (User) req.getSession().getAttribute("user");
        String username = user.getUsername();
        String action = req.getParameter("action");

        try (EntityManager em = EntityManagerFactoryProvider.getEMF().createEntityManager()) {
            RoomDAO dao = new RoomDAO(em);
            AuditLogDAO auditLogDAO = new AuditLogDAO(em);
            AuditLogService auditLogService = new AuditLogService(auditLogDAO);
            RoomService service = new RoomService(dao, auditLogService);

            if ("create".equals(action)) {
                String roomNumber   = req.getParameter("roomNumber");
                RoomType type       = RoomType.valueOf(req.getParameter("type"));
                double pricePerNight = Double.parseDouble(req.getParameter("pricePerNight"));
                Room room = new Room(roomNumber, type, pricePerNight);
                service.createRoom(room, username);
                resp.sendRedirect("rooms?success=created");

            } else if ("delete".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                service.deleteRoom(id, username);
                resp.sendRedirect("rooms?success=deleted");

            } else if ("updateStatus".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                RoomStatus status = RoomStatus.valueOf(req.getParameter("status"));
                service.updateStatus(id, status, username);
                resp.sendRedirect("rooms?success=updated");

            } else {
                resp.sendRedirect("rooms?error=Action inconnue");
            }

        } catch (Exception e) {
            resp.sendRedirect("rooms?error=" + e.getMessage());
        }
    }
}