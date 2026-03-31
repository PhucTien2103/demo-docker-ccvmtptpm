<-- view/admin/users.jsp -->
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User List</title>
</head>
<body>

<h2>User List</h2>

<a href="${pageContext.request.contextPath}/admin/users/add">
    Add User
</a>

<br/><br/>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Full Name</th>
        <th>Email</th>
        <th>Role</th>
        <th>Action</th>
    </tr>

    <c:forEach var="u" items="${list}">
        <tr>
            <td>${u.id}</td>
            <td>${u.fullName}</td>
            <td>${u.email}</td>
            <td>${u.role}</td>
            <td>
                <a href="${pageContext.request.contextPath}/admin/users/edit?id=${u.id}">
                    Edit
                </a>
                |
                <a href="${pageContext.request.contextPath}/admin/users/delete?id=${u.id}"
                   onclick="return confirm('Delete?')">
                    Delete
                </a>
                |
                <a href="${pageContext.request.contextPath}/my-profile?id=${u.id}">
                    View
                </a>
            </td>
        </tr>
    </c:forEach>

</table>

</body>
</html>