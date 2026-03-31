<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Leave Statistics</title>
</head>
<body>
	<h2>Leave Statistics</h2>

	<p>
		<a href="${pageContext.request.contextPath}/admin/dashboard">Back to dashboard</a>
	</p>

	<form method="get" action="${pageContext.request.contextPath}/admin/leave-statistics" style="margin-bottom: 12px;">
		<label for="fromDate">From date:</label>
		<input type="date" id="fromDate" name="fromDate" value="${selectedFromDate}" />

		<label for="toDate">To date:</label>
		<input type="date" id="toDate" name="toDate" value="${selectedToDate}" />

		<button type="submit">Filter</button>
		<a href="${pageContext.request.contextPath}/admin/leave-statistics">Reset</a>
	</form>

	<c:if test="${not empty error}">
		<p style="color: red;">${error}</p>
	</c:if>

	<h3>Overview</h3>
	<p>Total leave requests: <strong>${totalRequests}</strong></p>

	<table border="1" cellpadding="6" cellspacing="0">
		<tr>
			<th>Status</th>
			<th>Total</th>
		</tr>
		<c:forEach var="entry" items="${statusCounts}">
			<tr>
				<td>${entry.key}</td>
				<td>${entry.value}</td>
			</tr>
		</c:forEach>
	</table>

	<h3>Leave Type Statistics</h3>
	<table border="1" cellpadding="6" cellspacing="0">
		<tr>
			<th>Leave Type</th>
			<th>Total</th>
		</tr>
		<c:forEach var="entry" items="${leaveTypeCounts}">
			<tr>
				<td>${entry.key}</td>
				<td>${entry.value}</td>
			</tr>
		</c:forEach>
	</table>

	<h3>Filtered Requests</h3>
	<table border="1" cellpadding="6" cellspacing="0">
		<tr>
			<th>ID</th>
			<th>Employee</th>
			<th>Start Date</th>
			<th>End Date</th>
			<th>Reason</th>
			<th>Status</th>
		</tr>
		<c:forEach var="leaveRequest" items="${filteredLeaveRequests}">
			<tr>
				<td>${leaveRequest.id}</td>
				<td>${leaveRequest.user.fullName}</td>
				<td>${leaveRequest.startDate}</td>
				<td>${leaveRequest.endDate}</td>
				<td>${leaveRequest.reason}</td>
				<td>${leaveRequest.status}</td>
			</tr>
		</c:forEach>
		<c:if test="${empty filteredLeaveRequests}">
			<tr>
				<td colspan="6">No leave requests found.</td>
			</tr>
		</c:if>
	</table>
</body>
</html>
