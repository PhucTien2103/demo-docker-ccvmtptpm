package nhom13.vn.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import nhom13.vn.entity.User;
import nhom13.vn.service.IUserService;
import nhom13.vn.service.impl.UserServiceImpl;
import nhom13.vn.utils.EmailUtil;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    IUserService userService = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");

        User user = userService.findByEmail(email);

        if (user != null) {

            // tạo password mới
            String newPassword = String.valueOf((int)(Math.random()*100000));

            user.setPassword(newPassword);

            userService.update(user);

            boolean sent = EmailUtil.sendEmail(email, newPassword);

            if (sent) {

                req.setAttribute("message",
                        "Password mới đã gửi về email");

            } else {

                req.setAttribute("error",
                        "Không gửi được email");

            }

        } else {

            req.setAttribute("error",
                    "Email không tồn tại");

        }

        req.getRequestDispatcher("/view/auth/forgotpassword.jsp")
                .forward(req, resp);
    }
}