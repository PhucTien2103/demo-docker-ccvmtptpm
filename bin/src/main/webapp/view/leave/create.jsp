<-- /view/leave/create.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Create Leave Request</title>
</head>
<body>
	<h2>Create Leave Request</h2>
	<c:if test="${not empty leaveBalance}">
		<p>Remaining leave days: <strong>${leaveBalance.remainingDays}</strong> / ${leaveBalance.totalDays}</p>
	</c:if>

	<c:if test="${alert != null}">
		<p style="color: red">${alert}</p>
	</c:if>

	<form method="post"
		action="${pageContext.request.contextPath}/leave/insert">

		Start Date: <input type="date" name="startDate" /> <br />
		<br /> End Date: <input type="date" name="endDate" /> <br />
		<br /> Reason:
		<textarea name="reason"></textarea>
		<br />
		<br />

		<button type="submit">Submit</button>
	</form>
</body>
</html>