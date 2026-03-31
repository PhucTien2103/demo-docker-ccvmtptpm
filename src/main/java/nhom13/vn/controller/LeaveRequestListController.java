package nhom13.vn.controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import nhom13.vn.entity.LeaveRequest;
import nhom13.vn.entity.User;
import nhom13.vn.service.ILeaveRequestService;
import nhom13.vn.service.impl.LeaveRequestServiceImpl;

import java.io.IOException;
import java.util.List;
@WebServlet("/leave/list")
public class LeaveRequestListController extends HttpServlet {

    private ILeaveRequestService service = LeaveRequestServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("account");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String status = req.getParameter("status");
        List<LeaveRequest> list = service.getAllForViewer(user, status);

        String normalizedStatus = normalizeStatus(status);
        String pageTitle = (normalizedStatus == null)
                ? "All Leave Requests"
                : normalizedStatus + " Leave Requests";

        req.setAttribute("leaveList", list);
        req.setAttribute("selectedStatus", normalizedStatus == null ? "ALL" : normalizedStatus);
        req.setAttribute("pageTitle", pageTitle);

        req.getRequestDispatcher("/view/leave/list.jsp")
           .forward(req, resp);
    }

    private String normalizeStatus(String status) {
        if (status == null) {
            return null;
        }

        String normalized = status.trim().toUpperCase();
        if (normalized.isEmpty() || "ALL".equals(normalized)) {
            return null;
        }

        if (!"PENDING".equals(normalized)
                && !"APPROVED".equals(normalized)
                && !"REJECTED".equals(normalized)
                && !"CANCELLED".equals(normalized)) {
            return null;
        }

        return normalized;
    }
}
