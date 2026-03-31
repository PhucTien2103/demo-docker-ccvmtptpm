package nhom13.vn.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nhom13.vn.entity.Notification;
import nhom13.vn.entity.User;
import nhom13.vn.service.INotificationService;
import nhom13.vn.service.impl.NotificationServiceImpl;

@WebServlet("/notifications")
public class NotificationController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final INotificationService notificationService = NotificationServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("account");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Notification> notifications = notificationService.getByViewer(user);
        req.setAttribute("notifications", notifications);
        req.getRequestDispatcher("/view/notification/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("account");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int notificationId;
        try {
            notificationId = Integer.parseInt(req.getParameter("id"));
            if (notificationId <= 0) {
                throw new NumberFormatException("id must be positive");
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/notifications?msg=invalid");
            return;
        }

        boolean updated = notificationService.markAsReadForViewer(notificationId, user);
        if (updated) {
            resp.sendRedirect(req.getContextPath() + "/notifications?msg=read");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/notifications?msg=notfound");
    }
}
