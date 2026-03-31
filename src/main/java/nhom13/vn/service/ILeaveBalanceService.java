package nhom13.vn.service;

import nhom13.vn.entity.LeaveBalance;
import nhom13.vn.entity.User;

public interface ILeaveBalanceService {
	LeaveBalance findByUserId(int userId);

	LeaveBalance ensureDefaultForUser(User user);

	void updateRemainingDays(int userId, int remainingDays);
}

