package nhom13.vn.dao;

import nhom13.vn.entity.LeaveBalance;

public interface ILeaveBalanceDao {
    LeaveBalance findByUserId(int userId);

    void insert(LeaveBalance leaveBalance);

    void update(LeaveBalance leaveBalance);
}

