package nhom13.vn.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nhom13.vn.entity.LeaveRequest;
import nhom13.vn.entity.User;
import nhom13.vn.service.ILeaveRequestService;
import nhom13.vn.service.impl.LeaveRequestServiceImpl;

@WebServlet("/admin/leave-statistics")
public class AdminLeaveStatisticsController extends HttpServlet {

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

        if (!"SUPER_ADMIN".equals(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not allowed to view leave statistics");
            return;
        }

        LocalDate fromDate = parseDate(req.getParameter("fromDate"));
        LocalDate toDate = parseDate(req.getParameter("toDate"));

        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            req.setAttribute("error", "From date must be before or equal to To date.");
            fromDate = null;
            toDate = null;
        }

        final LocalDate filterFromDate = fromDate;
        final LocalDate filterToDate = toDate;

        List<LeaveRequest> filteredRequests = leaveRequestService.getAllForViewer(user, null)
                .stream()
                .filter(leaveRequest -> matchesDateRange(leaveRequest, filterFromDate, filterToDate))
                .collect(Collectors.toList());

        req.setAttribute("selectedFromDate", req.getParameter("fromDate"));
        req.setAttribute("selectedToDate", req.getParameter("toDate"));
        req.setAttribute("totalRequests", filteredRequests.size());
        req.setAttribute("statusCounts", buildStatusCounts(filteredRequests));
        req.setAttribute("leaveTypeCounts", buildLeaveTypeCounts(filteredRequests));
        req.setAttribute("filteredLeaveRequests", filteredRequests);

        req.getRequestDispatcher("/view/admin/leave-statistics.jsp").forward(req, resp);
    }

    private LocalDate parseDate(String rawDate) {
        if (rawDate == null || rawDate.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(rawDate.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private boolean matchesDateRange(LeaveRequest leaveRequest, LocalDate fromDate, LocalDate toDate) {
        if (leaveRequest == null || leaveRequest.getStartDate() == null) {
            return false;
        }

        LocalDate requestDate = toLocalDate(leaveRequest.getStartDate());

        if (fromDate != null && requestDate.isBefore(fromDate)) {
            return false;
        }

        if (toDate != null && requestDate.isAfter(toDate)) {
            return false;
        }

        return true;
    }

    private LocalDate toLocalDate(java.util.Date date) {
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }

        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private Map<String, Integer> buildStatusCounts(List<LeaveRequest> leaveRequests) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("PENDING", 0);
        counts.put("APPROVED", 0);
        counts.put("REJECTED", 0);
        counts.put("CANCELLED", 0);

        for (LeaveRequest leaveRequest : leaveRequests) {
            String status = leaveRequest.getStatus();
            if (counts.containsKey(status)) {
                counts.put(status, counts.get(status) + 1);
            }
        }

        return counts;
    }

    private Map<String, Integer> buildLeaveTypeCounts(List<LeaveRequest> leaveRequests) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("Sick Leave", 0);
        counts.put("Personal Leave", 0);
        counts.put("Family Leave", 0);
        counts.put("Other", 0);

        for (LeaveRequest leaveRequest : leaveRequests) {
            String leaveType = classifyLeaveType(leaveRequest.getReason());
            counts.put(leaveType, counts.get(leaveType) + 1);
        }

        return counts;
    }

    private String classifyLeaveType(String reason) {
        if (reason == null) {
            return "Other";
        }

        String normalizedReason = reason.toLowerCase(Locale.ROOT);

        if (normalizedReason.contains("om")
                || normalizedReason.contains("ốm")
                || normalizedReason.contains("benh")
                || normalizedReason.contains("bệnh")
                || normalizedReason.contains("kham")
                || normalizedReason.contains("khám")
                || normalizedReason.contains("sick")
                || normalizedReason.contains("health")) {
            return "Sick Leave";
        }

        if (normalizedReason.contains("gia dinh")
                || normalizedReason.contains("gia đình")
                || normalizedReason.contains("family")) {
            return "Family Leave";
        }

        if (normalizedReason.contains("ca nhan")
                || normalizedReason.contains("cá nhân")
                || normalizedReason.contains("viec rieng")
                || normalizedReason.contains("việc riêng")
                || normalizedReason.contains("personal")) {
            return "Personal Leave";
        }

        return "Other";
    }
}
