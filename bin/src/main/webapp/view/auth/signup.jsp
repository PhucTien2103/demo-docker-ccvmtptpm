<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/signup" method="post">

		Username: <input type="text" name="username" required /> Email: <input
			type="email" name="email" required /> Password: <input
			type="password" name="password" required /> <br>
		<br> Role: <select name="role">
			<option value="EMPLOYEE">Employee</option>
			<option value="MANAGER">Manager</option>
			<option value="SUPER_ADMIN">Super Admin</option>
		</select> <br>
		<br> Secret Code (chỉ cần cho Manager / Admin): <input
			type="text" name="secretCode" /> <br>
		<br>

		<button type="submit">Register</button>

	</form>

	<c:if test="${errorMsg != null}">
		<p style="color: red">${errorMsg}</p>
	</c:if>
</body>
</html>