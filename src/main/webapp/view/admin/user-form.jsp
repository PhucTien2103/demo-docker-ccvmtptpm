<-- view/admin/users=form.jsp -->
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Form</title>
</head>
<body>

<h2>User Form</h2>

<form method="post"
      action="${pageContext.request.contextPath}${user == null ? '/admin/users/insert' : '/admin/users/update'}">

    <c:if test="${user != null}">
        <input type="hidden" name="id" value="${user.id}" />
    </c:if>

    Username:
    <input type="text" name="username" value="${user.username}"
           ${user != null ? 'readonly' : ''} />
    <br/><br/>

    <c:if test="${user == null}">
        Password:
        <input type="password" name="password" />
        <br/><br/>
    </c:if>

    Full Name:
    <input type="text" name="fullName" value="${user.fullName}" />
    <br/><br/>

    Email:
    <input type="email" name="email" value="${user.email}" />
    <br/><br/>

    Role:
    <select name="role">
        <option value="EMPLOYEE" ${user.role == 'EMPLOYEE' ? 'selected' : ''}>EMPLOYEE</option>
        <option value="MANAGER" ${user.role == 'MANAGER' ? 'selected' : ''}>MANAGER</option>
    </select>
    <br/><br/>

    Remaining Leave Days:
    <input type="number" name="remainingDays" min="0" value="${user == null ? 12 : leaveRemainingDays}" />
    <br/><br/>

    <button type="submit">Save</button>
</form>

</body>
</html>