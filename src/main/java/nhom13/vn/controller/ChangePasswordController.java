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

@WebServlet("/change-password")
public class ChangePasswordController extends HttpServlet {

    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("account") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.getRequestDispatcher("/view/profile/change-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("account") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (oldPassword == null || newPassword == null || confirmPassword == null) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            req.getRequestDispatcher("/view/profile/change-password.jsp").forward(req, resp);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("error", "Mật khẩu mới và xác nhận không khớp.");
            req.getRequestDispatcher("/view/profile/change-password.jsp").forward(req, resp);
            return;
        }

        boolean updated = userService.changePasswordForSelf(currentUser, oldPassword, newPassword);
        if (!updated) {
            req.setAttribute("error", "Đổi mật khẩu thất bại. Kiểm tra mật khẩu cũ hoặc mật khẩu mới không hợp lệ/đã trùng.");
            req.getRequestDispatcher("/view/profile/change-password.jsp").forward(req, resp);
            return;
        }

        session.setAttribute("account", currentUser);
        resp.sendRedirect(req.getContextPath() + "/my-profile?success=1");
    }
}

