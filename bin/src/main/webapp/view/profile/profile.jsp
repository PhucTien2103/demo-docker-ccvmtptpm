<-- src/main/webapp/views/profile/profile.jsp -->
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Profile</title>
</head>
<body>

<h2>Profile</h2>

<c:if test="${param.success == 1}">
    <p style="color:green">Update thành công!</p>
</c:if>

<form action="${pageContext.request.contextPath}/update-profile" method="post">

    Username: <b>${user.username}</b><br><br>

    Full Name:
    <input type="text" name="fullName" value="${user.fullName}" />
    <br><br>

    Email:
    <input type="text" name="email" value="${user.email}" />
    <br><br>

    <button type="submit">Update</button>

</form>

</body>
</html>