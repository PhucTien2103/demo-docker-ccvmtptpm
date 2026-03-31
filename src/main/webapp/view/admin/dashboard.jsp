<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Super Admin Dashboard</title>
</head>
<body>
	<h2>Super Admin Dashboard</h2>

	<hr>
	<li>
		<a href="${pageContext.request.contextPath}/my-profile">
			My Profile
		</a>
	</li>
	</hr>

	<hr>
	<li>
		<a href="${pageContext.request.contextPath}/admin/users">
			Manage Users
		</a>
	</li>
	</hr>

	<ul>
		<li>
			<a href="${pageContext.request.contextPath}/admin/users">
				Manage Users
			</a>
		</li>

		<li>
			<a href="${pageContext.request.contextPath}/admin/leave-statistics">
				View Leave Statistics
			</a>
		</li>

		<li>
			<a href="${pageContext.request.contextPath}/notifications">
				View Notifications
			</a>
		</li>

		<li>
			<a href="${pageContext.request.contextPath}/leave/list">
				View Leave Requests
			</a>
		</li>

		<%--
		<li><a href="${pageContext.request.contextPath}/admin/company">Manage Company</a></li>
		<li><a href="${pageContext.request.contextPath}/admin/leave">Manage Leave</a></li>
		<li><a href="${pageContext.request.contextPath}/admin/reports">Manage Reports</a></li>
		<li><a href="${pageContext.request.contextPath}/admin/notifications">Manage Notifications</a></li>
		--%>
	</ul>
</body>
</html>