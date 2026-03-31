<-- view/mânger/dashboard.jsp -->
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
	<h2>Manager Dashboard</h2>

	<ul>
		<li><a href="${pageContext.request.contextPath}/my-profile">
				My Profile </a></li>
		<li><a href="${pageContext.request.contextPath}/notifications">
				View Notifications </a></li>

		<li><a
			href="${pageContext.request.contextPath}/manager/zemployeez">
				View Employees </a></li>
		<li><a href="${pageContext.request.contextPath}/leave/create">
				Request Leave </a></li>
				
		<li><a href="${pageContext.request.contextPath}/manager/status">
				Check Leave Status </a></li>
	    <%--
        <li><a href="${pageContext.request.contextPath}/manager/requests">
            Check Leave Requests </a></li>

        <li><a href="${pageContext.request.contextPath}/manager/approve">
            Approval Leave </a></li>

        <li><a href="${pageContext.request.contextPath}/manager/reports">
            Create Leave Reports </a></li>
        --%>

		<li><a href="${pageContext.request.contextPath}/leave/list">
				View Leave Requests </a></li>
		<li><a href="${pageContext.request.contextPath}/manager/balance">
				View Leave Balance </a></li>
	</ul>
	<c:if test="${message != null}">
		<p style="color: green">${message}</p>
		<c:remove var="message" scope="session" />
	</c:if>
</body>
</html>
