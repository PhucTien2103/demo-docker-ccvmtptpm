<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Check Leave Status</title>
</head>
<body>
    <h2>My Leave Status</h2>

    <p>
        <a href="${pageContext.request.contextPath}${dashboardPath}">Back to dashboard</a>
    </p>

    <form method="get" action="${pageContext.request.contextPath}${statusPath}" style="margin-bottom: 10px;">
        <label for="status">Status:</label>
        <select id="status" name="status">
            <option value="ALL" ${selectedStatus == 'ALL' ? 'selected' : ''}>ALL</option>
            <option value="PENDING" ${selectedStatus == 'PENDING' ? 'selected' : ''}>PENDING</option>
            <option value="APPROVED" ${selectedStatus == 'APPROVED' ? 'selected' : ''}>APPROVED</option>
            <option value="REJECTED" ${selectedStatus == 'REJECTED' ? 'selected' : ''}>REJECTED</option>
            <option value="CANCELLED" ${selectedStatus == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
        </select>
        <button type="submit">Filter</button>
    </form>

    <c:if test="${param.msg == 'cancelled'}">
        <p style="color: green;">Leave request cancelled successfully.</p>
    </c:if>
    <c:if test="${param.msg == 'notfound'}">
        <p style="color: red;">Unable to cancel: request not found or already processed.</p>
    </c:if>

    <table border="1">
        <tr>
            <th>ID</th>
            <th>Start</th>
            <th>End</th>
            <th>Reason</th>
            <th>Result</th>
            <th>Comment</th>
            <th>Action</th>
        </tr>

        <c:forEach var="lr" items="${leaveList}">
            <tr>
                <td>${lr.id}</td>
                <td>${lr.startDate}</td>
                <td>${lr.endDate}</td>
                <td>${lr.reason}</td>
                <td>${lr.status}</td>
                <td>${empty lr.reviewerComment ? '-' : lr.reviewerComment}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/leave/detail?id=${lr.id}">View</a>
                    <c:if test="${(sessionScope.account.role == 'EMPLOYEE' || sessionScope.account.role == 'MANAGER') && lr.status == 'PENDING'}">
                        <br />
                        <a href="${pageContext.request.contextPath}/leave/edit?id=${lr.id}">Edit</a>
                        <br />
                        <form method="post" action="${pageContext.request.contextPath}/leave/cancel" style="display:inline;">
                            <input type="hidden" name="id" value="${lr.id}" />
                            <button type="submit">Cancel</button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty leaveList}">
            <tr>
                <td colspan="7">You have not submitted any leave requests yet.</td>
            </tr>
        </c:if>
    </table>
</body>
</html>

