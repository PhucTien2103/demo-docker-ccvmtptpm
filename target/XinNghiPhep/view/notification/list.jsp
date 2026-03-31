<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Notifications</title>
</head>
<body>
	<h2>Notifications</h2>

	<p><a href="${pageContext.request.contextPath}/${sessionScope.account.role == 'SUPER_ADMIN' ? 'admin' : (sessionScope.account.role == 'MANAGER' ? 'manager' : 'employee')}/dashboard">Back to dashboard</a></p>

	<c:if test="${param.msg == 'read'}">
		<p style="color: green;">Notification marked as read successfully.</p>
	</c:if>
	<c:if test="${param.msg == 'invalid'}">
		<p style="color: red;">Invalid notification id.</p>
	</c:if>
	<c:if test="${param.msg == 'notfound'}">
		<p style="color: red;">Notification not found.</p>
	</c:if>

	<table border="1" cellpadding="6" cellspacing="0">
		<tr>
			<th>ID</th>
			<th>Content</th>
			<th>Sent Time</th>
			<th>Status</th>
			<th>Action</th>
		</tr>
		<c:forEach var="notification" items="${notifications}">
			<tr>
				<td>${notification.id}</td>
				<td>${notification.content}</td>
				<td>${notification.sentTime}</td>
				<td>${notification.read ? 'READ' : 'UNREAD'}</td>
				<td>
					<c:if test="${not notification.read}">
						<form method="post" action="${pageContext.request.contextPath}/notifications" style="display:inline;">
							<input type="hidden" name="id" value="${notification.id}" />
							<button type="submit">Mark as read</button>
						</form>
					</c:if>
					<c:if test="${notification.read}">
						<span>-</span>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		<c:if test="${empty notifications}">
			<tr>
				<td colspan="5">No notifications found.</td>
			</tr>
		</c:if>
	</table>
</body>
</html>
