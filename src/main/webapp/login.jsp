<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login - Hotelia</title>
    <link rel="stylesheet" href="css/login.css"/>
</head>
<body>
<div class="login-container">
    <h2>Hotelia</h2>
    <form action="login" method="post">
        <input type="text" name="username" placeholder="Username" required>
        <input type="password" name="password" placeholder="Password" required>
        <input type="submit" value="Login">
    </form>

    <% if(request.getParameter("error") != null){ %>
        <p class="error">Username or password incorrect</p>
    <% } %>
</div>
</body>
</html>