<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/forgot-password"
		method="post">

		Enter your email: <input type="email" name="email" required>

		<button type="submit">Reset Password</button>

	</form>

	<p style="color: red">${error}</p>
	<p style="color: green">${message}</p>
</body>
</html>