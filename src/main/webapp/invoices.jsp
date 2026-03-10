<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hotelia.model.Invoice" %>
<%@ page import="com.hotelia.model.User" %>
<%@ page import="com.hotelia.enums.Role" %>
<%@ page import="java.util.List" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    boolean isAdmin = user.getRole() == Role.ADMIN;
    String dashboardUrl = isAdmin ? "admin.jsp" : "receptionist.jsp";
    List<Invoice> invoices = (List<Invoice>) request.getAttribute("invoices");
    String success = request.getParameter("success");
    String error   = request.getParameter("error");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Factures - Hotelia</title>
    <link rel="stylesheet" href="css/invoices.css"/>
</head>
<body>

<div class="navbar">
    <h1>HOTELIA — Factures</h1>
    <div>
        <a href="<%= dashboardUrl %>" class="back">← Dashboard</a>
        <a href="logout">Déconnexion</a>
    </div>
</div>

<div class="container">


    <% if (success != null) { %>
        <div class="msg-success">
            <% if ("generated".equals(success)) { %>Facture générée avec succès.
            <% } else if ("paid".equals(success)) { %>Facture marquée comme payée.
            <% } %>
        </div>
    <% } %>
    <% if (error != null) { %>
        <div class="msg-error">Erreur : <%= error %></div>
    <% } %>


    <div class="form-section">
        <h3>Générer une facture</h3>
        <form action="invoices" method="post">
            <input type="hidden" name="action" value="generate"/>
            <input type="number" name="reservationId"
                   placeholder="ID de la réservation (COMPLETED)" required/>
            <button type="submit" class="btn btn-generate">Générer</button>
        </form>
    </div>


    <div class="table-section">
        <h3>Liste des factures</h3>
        <table>
            <tr>
                <th>#</th>
                <th>Client</th>
                <th>Chambre</th>
                <th>Nuits</th>
                <th>Total</th>
                <th>Date</th>
                <th>Statut</th>
                <th>Actions</th>
            </tr>

            <% if (invoices == null || invoices.isEmpty()) { %>
                <tr><td colspan="8" class="empty">Aucune facture.</td></tr>
            <% } else { for (Invoice inv : invoices) { %>
            <tr>
                <td>#<%= inv.getId() %></td>
                <td><%= inv.getReservation().getClient().getFullName() %></td>
                <td><%= inv.getReservation().getRoom().getRoomNumber() %></td>
                <td><%= inv.getReservation().getNumberOfNights() %></td>
                <td><%= inv.getTotalAmount() %> Ar</td>
                <td><%= inv.getIssueDate().toLocalDate() %></td>
                <td>
                    <% if (inv.isPaid()) { %>
                        <span class="badge badge-paid">PAYÉE</span>
                    <% } else { %>
                        <span class="badge badge-unpaid">EN ATTENTE</span>
                    <% } %>
                </td>
                <td>
                    <% if (!inv.isPaid()) { %>
                        <form action="invoices" method="post" style="display:inline">
                            <input type="hidden" name="action" value="pay"/>
                            <input type="hidden" name="id" value="<%= inv.getId() %>"/>
                            <button class="btn btn-pay"
                                onclick="return confirm('Marquer comme payée ?')">
                                Payer
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