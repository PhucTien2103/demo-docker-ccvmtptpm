<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Change Password</title>
</head>
<body>

<h2>Đổi mật khẩu</h2>

<p>
    <a href="${pageContext.request.contextPath}/my-profile">Back to profile</a>
</p>

<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/change-password">
    <label>Mật khẩu cũ</label><br>
    <input type="password" name="oldPassword" required /><br><br>

    <label>Mật khẩu mới</label><br>
    <input type="password" name="newPassword" required /><br>
    <small>Ít nhất 8 ký tự, có chữ hoa, chữ thường, số, không có khoảng trắng.</small>
    <br><br>

    <label>Xác nhận mật khẩu mới</label><br>
    <input type="password" name="confirmPassword" required /><br><br>

    <button type="submit">Update</button>
</form>

</body>
</html>