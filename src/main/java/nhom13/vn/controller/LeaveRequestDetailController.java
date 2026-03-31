package nhom13.vn.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nhom13.vn.entity.LeaveRequest;
import nhom13.vn.entity.User;
import nhom13.vn.service.ILeaveRequestService;
import nhom13.vn.service.impl.LeaveRequestServiceImpl;

@WebServlet("/leave/detail")
public class LeaveRequestDetailController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ILeaveRequestService service = LeaveRequestServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("account");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idRaw = req.getParameter("id");
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

        LeaveRequest leaveRequest = service.getDetailForViewer(leaveId, user);

        if (leaveRequest == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Leave request not found");
            return;
        }

        req.setAttribute("leaveRequest", leaveRequest);
        req.getRequestDispatcher("/view/leave/detail.jsp").forward(req, resp);
    }
}

