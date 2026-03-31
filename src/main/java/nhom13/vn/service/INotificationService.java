package nhom13.vn.service;

import java.util.List;

import nhom13.vn.entity.LeaveRequest;
import nhom13.vn.entity.Notification;
import nhom13.vn.entity.User;

public interface INotificationService {
    void create(Notification notification);

    List<Notification> getByViewer(User viewer);

    boolean markAsReadForViewer(int notificationId, User viewer);

    void notifyManagersAboutSubmittedLeaveRequest(User requester, LeaveRequest leaveRequest);
}
