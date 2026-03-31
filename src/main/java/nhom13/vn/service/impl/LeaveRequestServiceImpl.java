package nhom13.vn.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import nhom13.vn.dao.impl.LeaveRequestDaoImpl;
import nhom13.vn.service.ILeaveRequestService;
import nhom13.vn.dao.ILeaveRequestDao;
import nhom13.vn.entity.LeaveRequest;
import nhom13.vn.entity.User;
//Singleton
public class LeaveRequestServiceImpl implements ILeaveRequestService {

	private static LeaveRequestServiceImpl instance;
    private ILeaveRequestDao dao;

    public LeaveRequestServiceImpl() {
        dao = LeaveRequestDaoImpl.getInstance();
    }

    public static LeaveRequestServiceImpl getInstance() {
        if (instance == null) {
            instance = new LeaveRequestServiceImpl();
        }
        return instance;
    }

    @Override
    public void create(LeaveRequest lr) {
        dao.insert(lr);
    }

    @Override
    public List<LeaveRequest> getByUser(int userId) {
        return dao.findByUser(userId);
    }

    @Override
    public List<LeaveRequest> getPendingByUser(int userId) {
        return dao.findPendingByUser(userId);
    }

    @Override
    public List<LeaveRequest> getAll() {
        return dao.findAll();
    }

    @Override
    public List<LeaveRequest> getPendingAll() {
        return dao.findPendingAll();
    }

    @Override
    public List<LeaveRequest> getAllEmployees() {
        return dao.findAllEmployees();
    }

    @Override
    public List<LeaveRequest> getPendingEmployees() {
        return dao.findPendingEmployees();
    }

    @Override
    public List<LeaveRequest> getByUserWithReview(int userId, String status) {
        return dao.findByUserAndStatusWithReview(userId, normalizeStatus(status));
    }

    @Override
    public List<LeaveRequest> getAllForViewer(User viewer, String status) {
        if (viewer == null) {
            return List.of();
        }

        String normalizedStatus = normalizeStatus(status);
        String role = viewer.getRole();

        if ("EMPLOYEE".equals(role)) {
            return dao.findByUserAndStatusWithReview(viewer.getId(), normalizedStatus);
        }

        if ("MANAGER".equals(role)) {
            return dao.findAllEmployeesByStatus(normalizedStatus);
        }

        if ("SUPER_ADMIN".equals(role)) {
            return dao.findAllByStatus(normalizedStatus);
        }

        return List.of();
    }

    @Override
    public LeaveRequest getDetailForViewer(int leaveId, User viewer) {
        if (viewer == null) {
            return null;
        }

        String role = viewer.getRole();

        if ("EMPLOYEE".equals(role)) {
            return dao.findByIdForUser(leaveId, viewer.getId());
        }

        if ("MANAGER".equals(role)) {
            LeaveRequest ownRequest = dao.findByIdForUser(leaveId, viewer.getId());
            if (ownRequest != null) {
                return ownRequest;
            }
            return dao.findByIdForManager(leaveId);
        }

        if ("SUPER_ADMIN".equals(role)) {
            return dao.findById(leaveId);
        }

        return null;
    }

    @Override
    public boolean approveForViewer(int leaveId, User viewer) {
        return approveForViewer(leaveId, viewer, null);
    }

    @Override
    public boolean approveForViewer(int leaveId, User viewer, String note) {
        if (viewer == null || leaveId <= 0) {
            return false;
        }

        String role = viewer.getRole();

        if ("MANAGER".equals(role)) {
            return dao.approvePendingForManager(leaveId, viewer, note);
        }

        if ("SUPER_ADMIN".equals(role)) {
            return dao.approvePendingForAdmin(leaveId, viewer, note);
        }

        return false;
    }

    @Override
    public boolean rejectForViewer(int leaveId, User viewer) {
        return rejectForViewer(leaveId, viewer, null);
    }

    @Override
    public boolean rejectForViewer(int leaveId, User viewer, String note) {
        if (viewer == null || leaveId <= 0) {
            return false;
        }

        String role = viewer.getRole();

        if ("MANAGER".equals(role)) {
            return dao.rejectPendingForManager(leaveId, viewer, note);
        }

        if ("SUPER_ADMIN".equals(role)) {
            return dao.rejectPendingForAdmin(leaveId, viewer, note);
        }

        return false;
    }

    @Override
    public boolean cancelForViewer(int leaveId, User viewer) {
        if (viewer == null || leaveId <= 0) {
            return false;
        }

        if (!"EMPLOYEE".equals(viewer.getRole())) {
            return false;
        }

        return dao.cancelPendingForUser(leaveId, viewer.getId());
    }

    @Override
    public boolean updatePendingForEmployee(int leaveId, User employee, LocalDate startDate, LocalDate endDate,
            String reason) {
        if (employee == null || leaveId <= 0 ) {
            return false;
        }
        
        String role = employee.getRole() == null ? "" : employee.getRole().trim().toUpperCase();
        if (!"EMPLOYEE".equals(role) && !"MANAGER".equals(role)) {
            return false;
        }
        if (startDate == null || endDate == null || reason == null || reason.isBlank()) {
            return false;
        }
        if (endDate.isBefore(startDate)) {
            return false;
        }
        return dao.updatePendingForUser(leaveId, employee.getId(), Date.valueOf(startDate), Date.valueOf(endDate),
                reason.trim());
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
