package nhom13.vn.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nhom13.vn.entity.LeaveRequest;
import nhom13.vn.entity.User;
import nhom13.vn.service.ILeaveRequestService;
import nhom13.vn.service.impl.LeaveRequestServiceImpl;

@WebServlet({"/employee/status", "/manager/status"})
public class EmployeeLeaveStatusController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ILeaveRequestService leaveRequestService = LeaveRequestServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("account");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String role = user.getRole();
        if (!"EMPLOYEE".equals(role) && !"MANAGER".equals(role)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Only employees and managers can check leave status");
            return;
        }

        String selectedStatus = normalizeStatus(req.getParameter("status"));
        List<LeaveRequest> leaveRequests = leaveRequestService.getByUserWithReview(user.getId(), selectedStatus);

        String statusPath = "EMPLOYEE".equals(role) ? "/employee/status" : "/manager/status";
        String dashboardPath = "EMPLOYEE".equals(role) ? "/employee/dashboard" : "/manager/dashboard";

        req.setAttribute("leaveList", leaveRequests);
        req.setAttribute("selectedStatus", selectedStatus == null ? "ALL" : selectedStatus);
        req.setAttribute("statusPath", statusPath);
        req.setAttribute("dashboardPath", dashboardPath);

        req.getRequestDispatcher("/view/employee/status.jsp").forward(req, resp);
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

