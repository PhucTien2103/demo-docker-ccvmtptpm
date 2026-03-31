package nhom13.vn.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nhom13.vn.entity.User;
import nhom13.vn.service.IUserService;
import nhom13.vn.service.impl.UserServiceImpl;

import java.io.IOException;

@WebServlet("/admin/users/change-password")
public class AdminChangePasswordController extends HttpServlet {

    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User admin = (session != null) ? (User) session.getAttribute("account") : null;
        if (admin == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idRaw = req.getParameter("id");
        int targetId;
        try {
            targetId = Integer.parseInt(idRaw);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        User target = userService.findById(targetId);
        if (target == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("targetUser", target);
        req.getRequestDispatcher("/view/admin/change-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User admin = (session != null) ? (User) session.getAttribute("account") : null;
        if (admin == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idRaw = req.getParameter("id");
        int targetId;
        try {
            targetId = Integer.parseInt(idRaw);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (newPassword == null || confirmPassword == null) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            User target = userService.findById(targetId);
            req.setAttribute("targetUser", target);
            req.getRequestDispatcher("/view/admin/change-password.jsp").forward(req, resp);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("error", "Mật khẩu mới và xác nhận không khớp.");
            User target = userService.findById(targetId);
            req.setAttribute("targetUser", target);
            req.getRequestDispatcher("/view/admin/change-password.jsp").forward(req, resp);
            return;
        }

        boolean updated = userService.changePasswordAsAdmin(admin, targetId, newPassword);
        if (!updated) {
            req.setAttribute("error", "Đổi mật khẩu thất bại. Mật khẩu mới không hợp lệ/đã trùng hoặc không đủ quyền.");
            User target = userService.findById(targetId);
            req.setAttribute("targetUser", target);
            req.getRequestDispatcher("/view/admin/change-password.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/admin/users/edit?id=" + targetId);
    }
}

