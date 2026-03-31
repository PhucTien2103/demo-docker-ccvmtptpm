package nhom13.vn.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nhom13.vn.entity.User;
import nhom13.vn.service.ILeaveRequestService;
import nhom13.vn.service.impl.LeaveRequestServiceImpl;

@WebServlet("/leave/reject")
public class LeaveRejectController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ILeaveRequestService service = LeaveRequestServiceImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("account");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String role = user.getRole();
        if (!"MANAGER".equals(role) && !"SUPER_ADMIN".equals(role)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not allowed to reject leave requests");
            return;
        }

        String idRaw = req.getParameter("id");
        String note = req.getParameter("comment");
        int leaveId;
        try {
            leaveId = Integer.parseInt(idRaw);
            if (leaveId <= 0) {
                throw new NumberFormatException("id must be positive");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid leave request id");
            return;
        }

        boolean rejected = service.rejectForViewer(leaveId, user, note);
        if (rejected) {
            resp.sendRedirect(req.getContextPath() + "/leave/pending?msg=rejected");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/leave/pending?msg=notfound");
    }
}

