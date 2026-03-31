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

<hr/>
<c:if test="${sessionScope.account.id == user.id}">
    <p>
        <a href="${pageContext.request.contextPath}/change-password">Change password</a>
    </p>
</c:if>

</body>
</html>