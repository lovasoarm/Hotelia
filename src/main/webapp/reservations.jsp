<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hotelia.models.Reservation" %>
<%@ page import="com.hotelia.models.Client" %>
<%@ page import="com.hotelia.models.Room" %>
<%@ page import="com.hotelia.models.User" %>
<%@ page import="com.hotelia.enums.Role" %>
<%@ page import="com.hotelia.enums.ReservationStatus" %>
<%@ page import="java.util.List" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    boolean isAdmin = user.getRole() == Role.ADMIN;
    String dashboardUrl = isAdmin ? "admin.jsp" : "receptionist.jsp";
    List<Client> clients = (List<Client>) request.getAttribute("clients");
    List<Room> rooms     = (List<Room>) request.getAttribute("rooms");
    List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
    String success = request.getParameter("success");
    String error   = request.getParameter("error");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Réservations - Hotelia</title>
    <link rel="stylesheet" href="css/reservations.css"/>
</head>
<body>

<div class="navbar">
    <h1>HOTELIA — Réservations</h1>
    <div>
        <a href="<%= dashboardUrl %>" class="back">← Dashboard</a>
        <a href="logout">Déconnexion</a>
    </div>
</div>

<div class="container">


    <% if (success != null) { %>
        <div class="msg-success">
            <% if ("created".equals(success))   { %>Réservation créée.
            <% } else if ("confirmed".equals(success)) { %>Réservation confirmée.
            <% } else if ("cancelled".equals(success)) { %>Réservation annulée.
            <% } else if ("completed".equals(success)) { %>Réservation terminée.
            <% } %>
        </div>
    <% } %>
    <% if (error != null) { %>
        <div class="msg-error">Erreur : <%= error %></div>
    <% } %>


    <div class="form-section">
        <h3>Nouvelle réservation</h3>
        <form action="reservations" method="post">
            <input type="hidden" name="action" value="create"/>
            <select name="clientId" required>
                <option value="">-- Sélectionner un client --</option>
                <% if (clients != null) { for (Client c : clients) { %>
                    <option value="<%= c.getId() %>"><%= c.getFullName() %></option>
                <% } } %>
            </select>
            <select name="roomId" required>
                <option value="">-- Sélectionner une chambre --</option>
                <% if (rooms != null) { for (Room r : rooms) { %>
                    <option value="<%= r.getId() %>">
                        <%= r.getRoomNumber() %> — <%= r.getType() %> — <%= r.getPricePerNight() %> Ar
                    </option>
                <% } } %>
            </select>
            <input type="date" name="checkIn"  required/>
            <input type="date" name="checkOut" required/>
            <button type="submit" class="btn btn-create">Réserver</button>
        </form>
    </div>


    <div class="table-section">
        <h3>Liste des réservations</h3>
        <table>
            <tr>
                <th>Client</th>
                <th>Chambre</th>
                <th>Arrivée</th>
                <th>Départ</th>
                <th>Nuits</th>
                <th>Total</th>
                <th>Statut</th>
                <th>Actions</th>
            </tr>

            <% if (reservations == null || reservations.isEmpty()) { %>
                <tr><td colspan="8" class="empty">Aucune réservation.</td></tr>
            <% } else { for (Reservation r : reservations) {
                String badgeClass = "badge badge-" + r.getStatus().name().toLowerCase();
            %>
            <tr>
                <td><%= r.getClient().getFullName() %></td>
                <td><%= r.getRoom().getRoomNumber() %></td>
                <td><%= r.getCheckInDate() %></td>
                <td><%= r.getCheckOutDate() %></td>
                <td><%= r.getNumberOfNights() %></td>
                <td><%= r.calculateTotal() %> Ar</td>
                <td><span class="<%= badgeClass %>"><%= r.getStatus() %></span></td>
                <td>

                    <% if (r.getStatus() == ReservationStatus.PENDING) { %>
                        <form action="reservations" method="post" style="display:inline">
                            <input type="hidden" name="action" value="confirm"/>
                            <input type="hidden" name="id" value="<%= r.getId() %>"/>
                            <button class="btn btn-confirm">Confirmer</button>
                        </form>
                    <% } %>


                    <% if (r.getStatus() == ReservationStatus.CONFIRMED) { %>
                        <form action="reservations" method="post" style="display:inline">
                            <input type="hidden" name="action" value="complete"/>
                            <input type="hidden" name="id" value="<%= r.getId() %>"/>
                            <button class="btn btn-complete">Terminer</button>
                        </form>
                    <% } %>


                    <% if (r.getStatus() != ReservationStatus.CANCELLED
                        && r.getStatus() != ReservationStatus.COMPLETED) { %>
                        <form action="reservations" method="post" style="display:inline">
                            <input type="hidden" name="action" value="cancel"/>
                            <input type="hidden" name="id" value="<%= r.getId() %>"/>
                            <button class="btn btn-cancel"
                                onclick="return confirm('Annuler cette réservation ?')">
                                Annuler
                            </button>
                        </form>
                    <% } %>
                </td>
            </tr>
            <% } } %>
        </table>
    </div>

</div>
</body>
</html>