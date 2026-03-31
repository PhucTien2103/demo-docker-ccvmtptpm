<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Leave Balance</title>
</head>
<body>
    <h2>Leave Balance</h2>
    <c:if test="${not empty leaveBalance}">
        <table border="1">
            <tr>
                <th>Total Days</th>
                <td>${leaveBalance.totalDays}</td>
            </tr>
            <tr>
                <th>Used Days</th>
                <td>${leaveBalance.usedDays}</td>
            </tr>
            <tr>
                <th>Remaining Days</th>
                <td>${leaveBalance.remainingDays}</td>
            </tr>
        </table>
    </c:if>
    <p>
        <a href="${pageContext.request.contextPath}/leave/create">Create Leave Request</a>
    </p>
</body>
</html>
