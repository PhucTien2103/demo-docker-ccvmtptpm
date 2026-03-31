package nhom13.vn.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nhom13.vn.entity.User;

@WebServlet("/leave/pending")
public class LeavePendingController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("account");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String msg = req.getParameter("msg");
        String redirectUrl = req.getContextPath() + "/leave/list?status=PENDING";
        if (msg != null && !msg.isBlank()) {
            redirectUrl += "&msg=" + msg;
        }

        resp.sendRedirect(redirectUrl);
    }
}
