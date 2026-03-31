<!-- /view/leave/list.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>${empty pageTitle ? 'Leave Requests List' : pageTitle}</h2>
	<c:if test="${param.msg == 'approved'}">
		<p style="color: green;">Leave request approved successfully.</p>
	</c:if>
	<c:if test="${param.msg == 'rejected'}">
		<p style="color: green;">Leave request rejected successfully.</p>
	</c:if>
	<c:if test="${param.msg == 'cancelled'}">
		<p style="color: green;">Leave request cancelled successfully.</p>
	</c:if>
	<c:if test="${param.msg == 'notfound'}">
		<p style="color: red;">Unable to process: request not found or already processed.</p>
	</c:if>
	<p>
		<a href="${pageContext.request.contextPath}/leave/list?status=ALL">All</a> |
		<a href="${pageContext.request.contextPath}/leave/list?status=PENDING">Pending</a>
	</p>

	<form method="get" action="${pageContext.request.contextPath}/leave/list" style="margin-bottom: 10px;">
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

	<table border="1">
		<tr>
			<th>ID</th>
			<th>User</th>
			<th>Start</th>
			<th>End</th>
			<th>Reason</th>
			<th>Status</th>
			<th>Comment</th>
			<th>Action</th>
		</tr>

		<c:forEach var="lr" items="${leaveList}">
			<tr>
				<td>${lr.id}</td>
				<td>${lr.user.fullName}</td>
				<td>${lr.startDate}</td>
				<td>${lr.endDate}</td>
				<td>${lr.reason}</td>
				<td>${lr.status}</td>
				<td>${empty lr.reviewerComment ? '-' : lr.reviewerComment}</td>
				<td>
					<a href="${pageContext.request.contextPath}/leave/detail?id=${lr.id}">View</a>
					<c:if test="${sessionScope.account.role == 'EMPLOYEE' && lr.status == 'PENDING'}">
						<br />
						<a href="${pageContext.request.contextPath}/leave/edit?id=${lr.id}">Edit</a>
						<br />
						<form method="post" action="${pageContext.request.contextPath}/leave/cancel" style="display:inline;">
							<input type="hidden" name="id" value="${lr.id}" />
							<button type="submit">Cancel</button>
						</form>
					</c:if>
					<c:if test="${(sessionScope.account.role == 'MANAGER' || sessionScope.account.role == 'SUPER_ADMIN') && lr.status == 'PENDING'}">
						<br />
						<form method="post" style="display:inline;">
							<input type="hidden" name="id" value="${lr.id}" />
							<input type="text" name="comment" placeholder="Optional comment" />
							<button type="submit" formaction="${pageContext.request.contextPath}/leave/approve">Approve</button>
							<button type="submit" formaction="${pageContext.request.contextPath}/leave/reject">Reject</button>
						</form>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		<c:if test="${empty leaveList}">
			<tr>
				<td colspan="8">No leave requests found.</td>
			</tr>
		</c:if>
	</table>
</body>
</html>
