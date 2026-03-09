<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hotelia.model.Client" %>
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
%>

<!DOCTYPE html>
<html>
<head>
    <title>Clients - Hotelia</title>
    <link rel="stylesheet" href="css/clients.css"/>
</head>
<body>

<%-- NAVBAR --%>
<div class="navbar">
    <h1>HOTELIA — Clients</h1>
    <div>
        <a href="<%= dashboardUrl %>" class="back">← Dashboard</a>
        <a href="logout">Déconnexion</a>
    </div>
</div>

<div class="container">

    <%-- MESSAGES --%>
    <%
        String success = request.getParameter("success");
        String error   = request.getParameter("error");
    %>
    <% if ("created".equals(success)) { %>
        <div class="msg-success">Client créé avec succès.</div>
    <% } else if ("deleted".equals(success)) { %>
        <div class="msg-success">Client supprimé.</div>
    <% } else if ("updated".equals(success)) { %>
        <div class="msg-success">Client mis à jour.</div>
    <% } %>
    <% if (error != null) { %>
        <div class="msg-error">Erreur : <%= error %></div>
    <% } %>

    <%-- FORMULAIRE CREATION --%>
    <div class="form-section">
        <h3>Nouveau client</h3>
        <form action="clients" method="post">
            <input type="hidden" name="action" value="create"/>
            <input type="text"   name="firstName" placeholder="Prénom"    required/>
            <input type="text"   name="lastName"  placeholder="Nom"       required/>
            <input type="email"  name="email"     placeholder="Email"     required/>
            <input type="text"   name="phone"     placeholder="Téléphone"/>
            <input type="text"   name="address"   placeholder="Adresse"/>
            <button type="submit" class="btn btn-create">Créer</button>
        </form>
    </div>

    <%-- LISTE CLIENTS --%>
    <div class="table-section">
        <h3>Liste des clients</h3>
        <%
            List<Client> clients = (List<Client>) request.getAttribute("clients");
        %>
        <table>
            <tr>
                <th>Nom complet</th>
                <th>Email</th>
                <th>Téléphone</th>
                <th>Adresse</th>
                <% if (isAdmin) { %><th>Actions</th><% } %>
            </tr>

            <% if (clients == null || clients.isEmpty()) { %>
                <tr><td colspan="5" class="empty">Aucun client enregistré.</td></tr>
            <% } else { %>
                <% for (Client c : clients) { %>
                <tr>
                    <td><%= c.getFullName() %></td>
                    <td><%= c.getEmail() %></td>
                    <td><%= c.getPhone() %></td>
                    <td><%= c.getAddress() %></td>
                    <% if (isAdmin) { %>
                    <td>
                        <%-- SUPPRIMER --%>
                        <form action="clients" method="post" style="display:inline">
                            <input type="hidden" name="action" value="delete"/>
                            <input type="hidden" name="id" value="<%= c.getId() %>"/>
                            <button type="submit" class="btn btn-delete"
                                onclick="return confirm('Supprimer ce client ?')">
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