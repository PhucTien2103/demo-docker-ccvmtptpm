package nhom13.vn.dao.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import nhom13.vn.config.JPAConfig;
import nhom13.vn.dao.ILeaveRequestDao;
import nhom13.vn.entity.LeaveApproval;
import nhom13.vn.entity.LeaveBalance;
import nhom13.vn.entity.LeaveRequest;
import nhom13.vn.entity.User;

public class LeaveRequestDaoImpl implements ILeaveRequestDao {

    private static LeaveRequestDaoImpl instance;

    private LeaveRequestDaoImpl() {
    }

    @Override
    public void insert(LeaveRequest lr) {

        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            em.persist(lr);

            trans.commit();

        } catch (Exception e) {
            trans.rollback();
            e.printStackTrace();
        } finally {
            em.close(); // 🔥 BẮT BUỘC
        }
    }
    @Override
    public List<LeaveRequest> findByUser(int userId) {
        return findByUserAndStatus(userId, null);
    }

    @Override
    public List<LeaveRequest> findByUserAndStatus(int userId, String status) {

        EntityManager em = JPAConfig.getEntityManager();

        try {
            String jpql = "SELECT lr FROM LeaveRequest lr WHERE lr.user.id = :uid";
            if (status != null && !status.isBlank()) {
                jpql += " AND lr.status = :status";
            }
            jpql += " ORDER BY lr.startDate DESC";

            TypedQuery<LeaveRequest> query =
                em.createQuery(jpql, LeaveRequest.class);

            query.setParameter("uid", userId);
            if (status != null && !status.isBlank()) {
                query.setParameter("status", status);
            }

            return query.getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public List<LeaveRequest> findByUserAndStatusWithReview(int userId, String status) {
        EntityManager em = JPAConfig.getEntityManager();

        try {
            String jpql = "SELECT lr, la.note FROM LeaveRequest lr "
                    + "LEFT JOIN LeaveApproval la ON la.leaveRequest.id = lr.id "
                    + "WHERE lr.user.id = :uid";
            if (status != null && !status.isBlank()) {
                jpql += " AND lr.status = :status";
            }
            jpql += " ORDER BY lr.startDate DESC";

            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            query.setParameter("uid", userId);
            if (status != null && !status.isBlank()) {
                query.setParameter("status", status);
            }

            List<Object[]> rows = query.getResultList();
            List<LeaveRequest> result = new ArrayList<>(rows.size());

            for (Object[] row : rows) {
                LeaveRequest leaveRequest = (LeaveRequest) row[0];
                String reviewerComment = (String) row[1];
                leaveRequest.setReviewerComment(reviewerComment);
                result.add(leaveRequest);
            }

            return result;
        } finally {
            em.close();
        }
    }

    @Override
    public LeaveRequest findByIdForUser(int leaveId, int userId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT lr FROM LeaveRequest lr WHERE lr.id = :id AND lr.user.id = :uid",
                    LeaveRequest.class
            )
            .setParameter("id", leaveId)
            .setParameter("uid", userId)
            .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<LeaveRequest> findPendingByUser(int userId) {
        return findByUserAndStatus(userId, "PENDING");
    }
    @Override
    public List<LeaveRequest> findAll() {
        return findAllByStatus(null);
    }

    @Override
    public List<LeaveRequest> findAllByStatus(String status) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String jpql = "SELECT lr, la.note FROM LeaveRequest lr "
                    + "LEFT JOIN LeaveApproval la ON la.leaveRequest.id = lr.id";
            if (status != null && !status.isBlank()) {
                jpql += " WHERE lr.status = :status";
            }
            jpql += " ORDER BY lr.startDate DESC";

            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            if (status != null && !status.isBlank()) {
                query.setParameter("status", status);
            }

            return mapLeaveRequestsWithReviewComment(query.getResultList());

        } finally {
            em.close();
        }
    }

    @Override
    public LeaveRequest findById(int leaveId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.find(LeaveRequest.class, leaveId);
        } finally {
            em.close();
        }
    }

    @Override
    public List<LeaveRequest> findPendingAll() {
        return findAllByStatus("PENDING");
    }

    @Override
    public List<LeaveRequest> findAllEmployees() {
        return findAllEmployeesByStatus(null);
    }

    @Override
    public List<LeaveRequest> findAllEmployeesByStatus(String status) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String jpql = "SELECT lr, la.note FROM LeaveRequest lr "
                    + "LEFT JOIN LeaveApproval la ON la.leaveRequest.id = lr.id "
                    + "WHERE lr.user.role = 'EMPLOYEE'";
            if (status != null && !status.isBlank()) {
                jpql += " AND lr.status = :status";
            }
            jpql += " ORDER BY lr.startDate DESC";

            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            if (status != null && !status.isBlank()) {
                query.setParameter("status", status);
            }

            return mapLeaveRequestsWithReviewComment(query.getResultList());
        } finally {
            em.close();
        }
    }

    @Override
    public LeaveRequest findByIdForManager(int leaveId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT lr FROM LeaveRequest lr WHERE lr.id = :id AND lr.user.role = 'EMPLOYEE'",
                    LeaveRequest.class
            )
            .setParameter("id", leaveId)
            .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<LeaveRequest> findPendingEmployees() {
        return findAllEmployeesByStatus("PENDING");
    }

    @Override
    public boolean approvePendingForManager(int leaveId) {
        return approvePendingForManager(leaveId, null, null);
    }

    @Override
    public boolean approvePendingForManager(int leaveId, User reviewer, String note) {
        return approveAndConsumeDays(leaveId, true, reviewer, note);
    }

    @Override
    public boolean approvePendingForAdmin(int leaveId) {
        return approvePendingForAdmin(leaveId, null, null);
    }

    @Override
    public boolean approvePendingForAdmin(int leaveId, User reviewer, String note) {
        return approveAndConsumeDays(leaveId, false, reviewer, note);
    }

    @Override
    public boolean rejectPendingForManager(int leaveId) {
        return rejectPendingForManager(leaveId, null, null);
    }

    @Override
    public boolean rejectPendingForManager(int leaveId, User reviewer, String note) {
        return rejectPending(leaveId, true, reviewer, note);
    }

    @Override
    public boolean rejectPendingForAdmin(int leaveId) {
        return rejectPendingForAdmin(leaveId, null, null);
    }

    @Override
    public boolean rejectPendingForAdmin(int leaveId, User reviewer, String note) {
        return rejectPending(leaveId, false, reviewer, note);
    }

    @Override
    public boolean cancelPendingForUser(int leaveId, int userId) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            LeaveRequest leaveRequest = em.createQuery(
                            "SELECT lr FROM LeaveRequest lr WHERE lr.id = :id AND lr.user.id = :userId AND lr.status = 'PENDING'",
                            LeaveRequest.class
                    )
                    .setParameter("id", leaveId)
                    .setParameter("userId", userId)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (leaveRequest == null) {
                trans.rollback();
                return false;
            }

            leaveRequest.setStatus("CANCELLED");

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

    @Override
    public boolean updatePendingForUser(int leaveId, int userId, java.sql.Date startDate, java.sql.Date endDate,
            String reason) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            LeaveRequest leaveRequest = em.createQuery(
                            "SELECT lr FROM LeaveRequest lr WHERE lr.id = :id AND lr.user.id = :userId AND lr.status = 'PENDING'",
                            LeaveRequest.class
                    )
                    .setParameter("id", leaveId)
                    .setParameter("userId", userId)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (leaveRequest == null) {
                trans.rollback();
                return false;
            }

            LocalDate startLocal = startDate.toLocalDate();
            LocalDate endLocal = endDate.toLocalDate();
            if (endLocal.isBefore(startLocal)) {
                trans.rollback();
                return false;
            }
            int requestedDays = (int) ChronoUnit.DAYS.between(startLocal, endLocal) + 1;
            if (requestedDays <= 0) {
                trans.rollback();
                return false;
            }

            LeaveBalance leaveBalance;
            try {
                leaveBalance = em.createQuery(
                                "SELECT lb FROM LeaveBalance lb WHERE lb.user.id = :userId",
                                LeaveBalance.class
                        )
                        .setParameter("userId", userId)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .getSingleResult();
            } catch (NoResultException e) {
                String role = leaveRequest.getUser().getRole();
                if (!"EMPLOYEE".equals(role) && !"MANAGER".equals(role)) {
                    trans.rollback();
                    return false;
                }

                leaveBalance = new LeaveBalance();
                leaveBalance.setUser(leaveRequest.getUser());
                leaveBalance.setTotalDays(12);
                leaveBalance.setUsedDays(0);
                leaveBalance.setRemainingDays(12);
                leaveBalance.setLastResetYear(LocalDate.now().getYear());
                em.persist(leaveBalance);
            }

            if (leaveBalance.getRemainingDays() < requestedDays) {
                trans.rollback();
                return false;
            }

            leaveRequest.setStartDate(startDate);
            leaveRequest.setEndDate(endDate);
            leaveRequest.setReason(reason);

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

    private boolean approveAndConsumeDays(int leaveId, boolean managerScopeOnlyEmployee, User reviewer, String note) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            String jpql = "SELECT lr FROM LeaveRequest lr WHERE lr.id = :id AND lr.status = 'PENDING'";
            if (managerScopeOnlyEmployee) {
                jpql += " AND lr.user.role = 'EMPLOYEE'";
            }

            LeaveRequest leaveRequest;
            try {
                leaveRequest = em.createQuery(jpql, LeaveRequest.class)
                        .setParameter("id", leaveId)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .getSingleResult();
            } catch (NoResultException e) {
                trans.rollback();
                return false;
            }

            LeaveBalance leaveBalance;
            try {
                leaveBalance = em.createQuery(
                                "SELECT lb FROM LeaveBalance lb WHERE lb.user.id = :userId",
                                LeaveBalance.class
                        )
                        .setParameter("userId", leaveRequest.getUser().getId())
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .getSingleResult();
            } catch (NoResultException e) {
                String role = leaveRequest.getUser().getRole();
                if (!"EMPLOYEE".equals(role) && !"MANAGER".equals(role)) {
                    trans.rollback();
                    return false;
                }

                leaveBalance = new LeaveBalance();
                leaveBalance.setUser(leaveRequest.getUser());
                leaveBalance.setTotalDays(12);
                leaveBalance.setUsedDays(0);
                leaveBalance.setRemainingDays(12);
                leaveBalance.setLastResetYear(LocalDate.now().getYear());
                em.persist(leaveBalance);
            }

            int requestedDays = calculateRequestedDays(leaveRequest);
            if (requestedDays <= 0 || leaveBalance.getRemainingDays() < requestedDays) {
                trans.rollback();
                return false;
            }

            leaveRequest.setStatus("APPROVED");
            upsertApproval(em, leaveRequest, reviewer, "APPROVED", note);
            leaveBalance.setUsedDays(leaveBalance.getUsedDays() + requestedDays);
            leaveBalance.setRemainingDays(leaveBalance.getRemainingDays() - requestedDays);

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

    private boolean rejectPending(int leaveId, boolean managerScopeOnlyEmployee, User reviewer, String note) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            String jpql = "SELECT lr FROM LeaveRequest lr WHERE lr.id = :id AND lr.status = 'PENDING'";
            if (managerScopeOnlyEmployee) {
                jpql += " AND lr.user.role = 'EMPLOYEE'";
            }

            LeaveRequest leaveRequest;
            try {
                leaveRequest = em.createQuery(jpql, LeaveRequest.class)
                        .setParameter("id", leaveId)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .getSingleResult();
            } catch (NoResultException e) {
                trans.rollback();
                return false;
            }

            leaveRequest.setStatus("REJECTED");
            upsertApproval(em, leaveRequest, reviewer, "REJECTED", note);

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

    private void upsertApproval(EntityManager em, LeaveRequest leaveRequest, User reviewer, String decision, String note) {
        LeaveApproval approval;
        try {
            approval = em.createQuery(
                            "SELECT la FROM LeaveApproval la WHERE la.leaveRequest.id = :leaveId",
                            LeaveApproval.class
                    )
                    .setParameter("leaveId", leaveRequest.getId())
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getSingleResult();
        } catch (NoResultException e) {
            approval = new LeaveApproval();
            approval.setLeaveRequest(leaveRequest);
        }

        if (reviewer != null && reviewer.getId() > 0) {
            approval.setManager(em.getReference(User.class, reviewer.getId()));
        }
        approval.setDecision(decision);
        approval.setApprovalTime(new Date());
        approval.setNote(normalizeNote(note));

        if (approval.getId() == 0) {
            em.persist(approval);
        }
    }

    private String normalizeNote(String note) {
        if (note == null) {
            return null;
        }

        String trimmed = note.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private List<LeaveRequest> mapLeaveRequestsWithReviewComment(List<Object[]> rows) {
        List<LeaveRequest> result = new ArrayList<>(rows.size());
        for (Object[] row : rows) {
            LeaveRequest leaveRequest = (LeaveRequest) row[0];
            String reviewerComment = (String) row[1];
            leaveRequest.setReviewerComment(reviewerComment);
            result.add(leaveRequest);
        }
        return result;
    }

    private int calculateRequestedDays(LeaveRequest leaveRequest) {
        LocalDate startDate = LocalDate.ofInstant(
                java.time.Instant.ofEpochMilli(leaveRequest.getStartDate().getTime()),
                ZoneId.systemDefault()
        );
        LocalDate endDate = LocalDate.ofInstant(
                java.time.Instant.ofEpochMilli(leaveRequest.getEndDate().getTime()),
                ZoneId.systemDefault()
        );

        if (endDate.isBefore(startDate)) {
            return 0;
        }

        return (int) (ChronoUnit.DAYS.between(startDate, endDate) + 1);
    }

    public static LeaveRequestDaoImpl getInstance() {
        if (instance == null) {
            instance = new LeaveRequestDaoImpl();
        }
        return instance;
    }
}
