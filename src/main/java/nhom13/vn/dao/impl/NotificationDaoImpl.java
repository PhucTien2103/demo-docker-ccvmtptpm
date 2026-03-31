package nhom13.vn.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import nhom13.vn.config.JPAConfig;
import nhom13.vn.dao.INotificationDao;
import nhom13.vn.entity.Notification;

public class NotificationDaoImpl implements INotificationDao {

    private static NotificationDaoImpl instance;

    private NotificationDaoImpl() {
    }

    public static NotificationDaoImpl getInstance() {
        if (instance == null) {
            instance = new NotificationDaoImpl();
        }
        return instance;
    }

    @Override
    public void insert(Notification notification) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();
            em.persist(notification);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Notification> findByReceiver(int receiverId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT n FROM Notification n WHERE n.receiver.id = :receiverId ORDER BY n.sentTime DESC, n.id DESC",
                            Notification.class
                    )
                    .setParameter("receiverId", receiverId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean markAsRead(int notificationId, int receiverId) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            Notification notification = em.createQuery(
                            "SELECT n FROM Notification n WHERE n.id = :notificationId AND n.receiver.id = :receiverId",
                            Notification.class
                    )
                    .setParameter("notificationId", notificationId)
                    .setParameter("receiverId", receiverId)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (notification == null) {
                trans.rollback();
                return false;
            }

            if (!notification.isRead()) {
                notification.setRead(true);
            }

            trans.commit();
            return true;
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }
}
