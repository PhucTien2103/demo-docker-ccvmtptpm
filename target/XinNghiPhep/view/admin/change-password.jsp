<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Change Password</title>
</head>
<body>

<h2>Đổi mật khẩu (Admin)</h2>

<p>
    <a href="${pageContext.request.contextPath}/admin/users">Back to users</a>
</p>

<c:if test="${not empty targetUser}">
    <p>
        User: <b>${targetUser.username}</b> (Role: <b>${targetUser.role}</b>)
    </p>
</c:if>

<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/admin/users/change-password">
    <input type="hidden" name="id" value="${targetUser.id}" />

    <label>Mật khẩu mới</label><br>
    <input type="password" name="newPassword" required /><br>
    <small>Ít nhất 8 ký tự, có chữ hoa, chữ thường, số, không có khoảng trắng.</small>
    <br><br>

    <label>Xác nhận mật khẩu mới</label><br>
    <input type="password" name="confirmPassword" required /><br><br>

    <button type="submit">Cập nhật</button>
</form>

</body>
</html>

