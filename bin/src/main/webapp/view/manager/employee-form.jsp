<-- view/mânger/employee-form.jsp -->
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Form</title>
</head>
<body>

<h2>Employee Form</h2>

<form method="post"
      action="${pageContext.request.contextPath}${user == null ? '/manager/zemployeez/insert' : '/manager/zemployeez/update'}">

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

    <button type="submit">Save</button>
</form>

</body>
</html>