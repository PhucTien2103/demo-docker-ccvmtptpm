package nhom13.vn.controller;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import nhom13.vn.service.IUserService;
import nhom13.vn.service.impl.UserServiceImpl;
@WebServlet(urlPatterns = {"/signup"})
public class SignupController extends HttpServlet {

    IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/view/auth/signup.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        String role = req.getParameter("role");
        String secretCode = req.getParameter("secretCode");

        // kiểm tra secret code
        if ("MANAGER".equals(role) || "SUPER_ADMIN".equals(role)) {

            if (secretCode == null || !secretCode.equals("1d3t")) {

                req.setAttribute("errorMsg", "Mã bí mật không đúng");
                req.getRequestDispatcher("/view/auth/signup.jsp").forward(req, resp);
                return;

            }
        }

        int result = userService.register(username, password, email, role);

        if (result == 0) {

            resp.sendRedirect(req.getContextPath() + "/login?msg=success");

        } else {

            req.setAttribute("errorMsg", "Username hoặc Email đã tồn tại");
            req.getRequestDispatcher("/view/auth/signup.jsp").forward(req, resp);

        }
    }
}