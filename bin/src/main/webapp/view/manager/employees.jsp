<-- view/mânger/employees.jsp -->
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee List</title>
</head>
<body>

<h2>Employee List</h2>

<a href="${pageContext.request.contextPath}/manager/zemployeez/add">
    Add Employee
</a>

<br/><br/>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Full Name</th>
        <th>Email</th>
        <th>Action</th>
    </tr>

    <c:forEach var="u" items="${list}">
        <tr>
            <td>${u.id}</td>
            <td>${u.fullName}</td>
            <td>${u.email}</td>
            <td>
                <a href="${pageContext.request.contextPath}/manager/zemployeez/edit?id=${u.id}">
                    Edit
                </a>

                |

                <a href="${pageContext.request.contextPath}/manager/zemployeez/delete?id=${u.id}"
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