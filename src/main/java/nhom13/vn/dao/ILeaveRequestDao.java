package nhom13.vn.dao;

import java.sql.Date;
import java.util.List;

import nhom13.vn.entity.LeaveRequest;
import nhom13.vn.entity.User;

public interface ILeaveRequestDao {
    void insert(LeaveRequest lr);
    	
    List<LeaveRequest> findByUser(int userId);

    List<LeaveRequest> findByUserAndStatus(int userId, String status);

    List<LeaveRequest> findByUserAndStatusWithReview(int userId, String status);

    LeaveRequest findByIdForUser(int leaveId, int userId);

    List<LeaveRequest> findPendingByUser(int userId);
    
    List<LeaveRequest> findAll(); // 🔥 admin

    List<LeaveRequest> findAllByStatus(String status);

    LeaveRequest findById(int leaveId);

    List<LeaveRequest> findPendingAll();

    List<LeaveRequest> findAllEmployees(); // 🔥 manager

    List<LeaveRequest> findAllEmployeesByStatus(String status);

    LeaveRequest findByIdForManager(int leaveId);

    List<LeaveRequest> findPendingEmployees();

    boolean approvePendingForManager(int leaveId);

    boolean approvePendingForManager(int leaveId, User reviewer, String note);

    boolean approvePendingForAdmin(int leaveId);

    boolean approvePendingForAdmin(int leaveId, User reviewer, String note);

    boolean rejectPendingForManager(int leaveId);

    boolean rejectPendingForManager(int leaveId, User reviewer, String note);

    boolean rejectPendingForAdmin(int leaveId);

    boolean rejectPendingForAdmin(int leaveId, User reviewer, String note);

    boolean cancelPendingForUser(int leaveId, int userId);

    /**
     * Updates start/end/reason only when the request belongs to {@code userId} and status is PENDING.
     * Re-validates remaining leave balance against the new date range inside the transaction.
     */
    boolean updatePendingForUser(int leaveId, int userId, Date startDate, Date endDate, String reason);
}
