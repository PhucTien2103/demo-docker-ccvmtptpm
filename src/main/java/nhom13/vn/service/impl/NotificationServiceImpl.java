package nhom13.vn.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nhom13.vn.dao.INotificationDao;
import nhom13.vn.dao.impl.NotificationDaoImpl;
import nhom13.vn.entity.LeaveRequest;
import nhom13.vn.entity.Notification;
import nhom13.vn.entity.User;
import nhom13.vn.service.INotificationService;
import nhom13.vn.service.IUserService;

public class NotificationServiceImpl implements INotificationService {

    private static NotificationServiceImpl instance;

    private final INotificationDao notificationDao;
    private final IUserService userService;

    private NotificationServiceImpl() {
        this.notificationDao = NotificationDaoImpl.getInstance();
        this.userService = new UserServiceImpl();
    }

    public static NotificationServiceImpl getInstance() {
        if (instance == null) {
            instance = new NotificationServiceImpl();
        }
        return instance;
    }

    @Override
    public void create(Notification notification) {
        if (notification == null || notification.getReceiver() == null || notification.getReceiver().getId() <= 0) {
            return;
        }

        if (notification.getSentTime() == null) {
            notification.setSentTime(new Date());
        }

        notificationDao.insert(notification);
    }

    @Override
    public List<Notification> getByViewer(User viewer) {
        if (viewer == null || viewer.getId() <= 0) {
            return List.of();
        }

        return notificationDao.findByReceiver(viewer.getId());
    }

    @Override
    public boolean markAsReadForViewer(int notificationId, User viewer) {
        if (viewer == null || viewer.getId() <= 0 || notificationId <= 0) {
            return false;
        }

        return notificationDao.markAsRead(notificationId, viewer.getId());
    }

    @Override
    public void notifyManagersAboutSubmittedLeaveRequest(User requester, LeaveRequest leaveRequest) {
        if (requester == null || leaveRequest == null || requester.getId() <= 0) {
            return;
        }

        if (!"EMPLOYEE".equals(requester.getRole())) {
            return;
        }

        if (requester.getCompany() == null || requester.getCompany().getId() <= 0) {
            return;
        }

        List<User> managers = userService.findByRole("MANAGER");
        if (managers == null || managers.isEmpty()) {
            return;
        }

        int companyId = requester.getCompany().getId();
        String content = buildSubmittedRequestMessage(requester, leaveRequest);

        for (User manager : managers) {
            if (manager == null || manager.getId() <= 0 || manager.getCompany() == null) {
                continue;
            }

            if (manager.getCompany().getId() != companyId) {
                continue;
            }

            Notification notification = new Notification();
            notification.setReceiver(manager);
            notification.setContent(content);
            notification.setRead(false);
            notification.setSentTime(new Date());
            create(notification);
        }
    }

    private String buildSubmittedRequestMessage(User requester, LeaveRequest leaveRequest) {
        String requesterName = requester.getFullName();
        if (requesterName == null || requesterName.isBlank()) {
            requesterName = requester.getUsername();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return "Nhân viên " + requesterName
                + " vừa gửi đơn nghỉ phép từ "
                + dateFormat.format(leaveRequest.getStartDate())
                + " đến "
                + dateFormat.format(leaveRequest.getEndDate()) + ".";
    }
}
