<!-- src/main/webapp/view/home -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<c:choose>

		<c:when test="${sessionScope.account != null}">

Chào bạn, ${sessionScope.account.fullName}

<a href="${pageContext.request.contextPath}/logout"> Đăng xuất </a>

		</c:when>

		<c:otherwise>

			<a href="${pageContext.request.contextPath}/login"> Đăng nhập </a>

		</c:otherwise>

	</c:choose>

</body>
</html>