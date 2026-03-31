package nhom13.vn.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nhom13.vn.entity.LeaveBalance;
import nhom13.vn.entity.User;
import nhom13.vn.service.ILeaveBalanceService;
import nhom13.vn.service.impl.LeaveBalanceServiceImpl;

@WebServlet({
        "/leave/balance",
        "/employee/balance",
        "/manager/balance"
})
public class LeaveBalanceController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ILeaveBalanceService leaveBalanceService = LeaveBalanceServiceImpl.getInstance();

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
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Role does not have leave balance");
            return;
        }

        LeaveBalance leaveBalance = leaveBalanceService.ensureDefaultForUser(user);
        req.setAttribute("leaveBalance", leaveBalance);
        req.getRequestDispatcher("/view/leave/balance.jsp").forward(req, resp);
    }
}

