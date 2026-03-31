<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Leave Request</title>
</head>
<body>
	<h2>Edit Leave Request</h2>

	<p>
		<a href="${pageContext.request.contextPath}/leave/detail?id=${leaveRequest.id}">Back to detail</a>
	</p>

	<c:if test="${not empty leaveBalance}">
		<p>Remaining leave days: <strong>${leaveBalance.remainingDays}</strong> / ${leaveBalance.totalDays}</p>
	</c:if>

	<c:if test="${alert != null}">
		<p style="color: red">${alert}</p>
	</c:if>

	<c:if test="${not empty leaveRequest}">
		<fmt:formatDate var="startIso" value="${leaveRequest.startDate}" pattern="yyyy-MM-dd" />
		<fmt:formatDate var="endIso" value="${leaveRequest.endDate}" pattern="yyyy-MM-dd" />
		<form method="post"
			action="${pageContext.request.contextPath}/leave/update">
			<input type="hidden" name="id" value="${leaveRequest.id}" />

			Start Date:
			<input type="date" name="startDate" value="${startIso}" />
			<br /><br />
			End Date:
			<input type="date" name="endDate" value="${endIso}" />
			<br /><br />
			Reason:
			<textarea name="reason"><c:out value="${leaveRequest.reason}" /></textarea>
			<br /><br />

			<button type="submit">Save</button>
		</form>
	</c:if>
</body>
</html>
