<!-- src/main/webapp/view/auth/login.jsp -->
<%@ page contentType="text/html;charset=UTF-8"%>

<html>
<head>
<title>Login</title>
</head>

<body>

	<h2>Login</h2>

	<form action="${pageContext.request.contextPath}/login" method="post">

		Username: <input type="text" name="username" /><br /> Password: <input
			type="password" name="password" /><br /> <input type="submit"
			value="Login" />

	</form>

	<p style="color: red">${alert}</p>

	<br>

	<a href="${pageContext.request.contextPath}/view/auth/forgotpassword.jsp">
		Forgot Password </a>

	<br>

	<a href="${pageContext.request.contextPath}/view/auth/signup.jsp"> Sign
		Up </a>

</body>
</html>