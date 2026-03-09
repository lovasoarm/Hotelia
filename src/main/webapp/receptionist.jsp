<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hotelia.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if(user == null || !user.getRole().name().equals("RECEPTIONIST")){
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html>
<head>
    <title>Receptionist Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; background: #f5f5f5; }
        h2 { color: #333; }
        a.logout { display: inline-block; margin-top: 20px; padding: 8px 12px; background: orange; color: white; text-decoration: none; border-radius: 4px; }
        a.logout:hover { background: #3498db; }
    </style>
</head>
<body>
<h2>Welcome Receptionist: <%= user.getUsername() %></h2>
<a href="logout" class="logout">Logout</a>
</body>
</html>