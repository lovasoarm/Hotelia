<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hotelia.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if(user == null || !user.getRole().name().equals("ADMIN")){
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html>
<head>
    <title>Admin Dashboard - Hotelia</title>
    <link rel="stylesheet" href="css/dashboard.css"/>
</head>
<body>

<div class="navbar">
    <h1>HOTELIA</h1>
    <div>
        <span>Admin : <%= user.getUsername() %></span>
        <a href="logout">Déconnexion</a>
    </div>
</div>

<div class="container">
    <p class="welcome">Tableau de bord — accès complet</p>
    <div class="grid">

        <a href="clients" class="card">
            <div class="icon">👥</div>
            <div class="label">Clients</div>
            <div class="desc">Créer, modifier, supprimer</div>
        </a>

        <a href="rooms" class="card">
            <div class="icon">🛏</div>
            <div class="label">Chambres</div>
            <div class="desc">Gérer les chambres et statuts</div>
        </a>

        <a href="reservations" class="card">
            <div class="icon">📅</div>
            <div class="label">Réservations</div>
            <div class="desc">Créer, confirmer, annuler</div>
        </a>

        <a href="invoices" class="card">
            <div class="icon">💰</div>
            <div class="label">Factures</div>
            <div class="desc">Générer et consulter</div>
        </a>

    </div>
</div>

</body>
</html>