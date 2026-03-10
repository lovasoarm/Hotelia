<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hotelia.model.Room" %>
<%@ page import="com.hotelia.model.User" %>
<%@ page import="com.hotelia.enums.Role" %>
<%@ page import="com.hotelia.enums.RoomType" %>
<%@ page import="com.hotelia.enums.RoomStatus" %>
<%@ page import="java.util.List" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    boolean isAdmin = user.getRole() == Role.ADMIN;
    String dashboardUrl = isAdmin ? "admin.jsp" : "receptionist.jsp";
%>

<!DOCTYPE html>
<html>
<head>
    <title>Chambres - Hotelia</title>
    <link rel="stylesheet" href="css/rooms.css"/>
</head>
<body>

<div class="navbar">
    <h1>HOTELIA — Chambres</h1>
    <div>
        <a href="<%= dashboardUrl %>" class="back">← Dashboard</a>
        <a href="logout">Déconnexion</a>
    </div>
</div>

<div class="container">

    <%-- MESSAGES --%>
    <% String success = request.getParameter("success"); %>
    <% String error   = request.getParameter("error"); %>
    <% if (success != null) { %>
        <div class="msg-success">
            <% if ("created".equals(success)) { %>Chambre créée avec succès.
            <% } else if ("deleted".equals(success)) { %>Chambre supprimée.
            <% } else if ("updated".equals(success)) { %>Statut mis à jour.
            <% } %>
        </div>
    <% } %>
    <% if (error != null) { %>
        <div class="msg-error">Erreur : <%= error %></div>
    <% } %>


    <% if (isAdmin) { %>
    <div class="form-section">
        <h3>Nouvelle chambre</h3>
        <form action="rooms" method="post">
            <input type="hidden" name="action" value="create"/>
            <input type="text" name="roomNumber" placeholder="Numéro (ex: 101)" required/>
            <select name="type">
                <% for (RoomType t : RoomType.values()) { %>
                    <option value="<%= t.name() %>"><%= t.name() %></option>
                <% } %>
            </select>
            <input type="number" name="pricePerNight"
                   placeholder="Prix par nuit" step="0.01" required/>
            <button type="submit" class="btn btn-create">Ajouter</button>
        </form>
    </div>
    <% } %>


    <div class="table-section">
        <h3>Liste des chambres</h3>
        <% List<Room> rooms = (List<Room>) request.getAttribute("rooms"); %>
        <table>
            <tr>
                <th>Numéro</th>
                <th>Type</th>
                <th>Prix/nuit</th>
                <th>Statut</th>
                <% if (isAdmin) { %><th>Actions</th><% } %>
            </tr>

            <% if (rooms == null || rooms.isEmpty()) { %>
                <tr><td colspan="5" class="empty">Aucune chambre enregistrée.</td></tr>
            <% } else { %>
                <% for (Room r : rooms) { %>
                <tr>
                    <td><%= r.getRoomNumber() %></td>
                    <td><%= r.getType().name() %></td>
                    <td><%= r.getPricePerNight() %> Ar</td>
                    <td>
                        <%-- Badge statut coloré --%>
                        <% String badgeClass = "badge ";
                           if (r.getStatus() == RoomStatus.AVAILABLE)
                               badgeClass += "badge-available";
                           else if (r.getStatus() == RoomStatus.OCCUPIED)
                               badgeClass += "badge-occupied";
                           else
                               badgeClass += "badge-maintenance";
                        %>
                        <span class="<%= badgeClass %>"><%= r.getStatus().name() %></span>
                    </td>
                    <% if (isAdmin) { %>
                    <td>

                        <form action="rooms" method="post" style="display:inline">
                            <input type="hidden" name="action" value="updateStatus"/>
                            <input type="hidden" name="id" value="<%= r.getId() %>"/>
                            <select name="status">
                                <% for (RoomStatus s : RoomStatus.values()) { %>
                                    <option value="<%= s.name() %>"
                                        <%= s == r.getStatus() ? "selected" : "" %>>
                                        <%= s.name() %>
                                    </option>
                                <% } %>
                            </select>
                            <button type="submit" class="btn btn-status">Modifier</button>
                        </form>


                        <form action="rooms" method="post" style="display:inline">
                            <input type="hidden" name="action" value="delete"/>
                            <input type="hidden" name="id" value="<%= r.getId() %>"/>
                            <button type="submit" class="btn btn-delete"
                                onclick="return confirm('Supprimer cette chambre ?')">
                                Supprimer
                            </button>
                        </form>
                    </td>
                    <% } %>
                </tr>
                <% } %>
            <% } %>
        </table>
    </div>

</div>
</body>
</html>