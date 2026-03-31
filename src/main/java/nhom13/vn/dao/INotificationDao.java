package nhom13.vn.dao;

import java.util.List;

import nhom13.vn.entity.Notification;

public interface INotificationDao {
    void insert(Notification notification);

    List<Notification> findByReceiver(int receiverId);

    boolean markAsRead(int notificationId, int receiverId);
}
