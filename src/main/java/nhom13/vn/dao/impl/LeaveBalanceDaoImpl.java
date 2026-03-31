package nhom13.vn.dao.impl;

import java.time.LocalDate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import nhom13.vn.config.JPAConfig;
import nhom13.vn.dao.ILeaveBalanceDao;
import nhom13.vn.entity.LeaveBalance;

public class LeaveBalanceDaoImpl implements ILeaveBalanceDao {

	@Override
	public LeaveBalance findByUserId(int userId) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			return em.createQuery("SELECT lb FROM LeaveBalance lb WHERE lb.user.id = :userId", LeaveBalance.class)
					.setParameter("userId", userId)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	@Override
	public void insert(LeaveBalance leaveBalance) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			if (leaveBalance.getLastResetYear() <= 0) {
				leaveBalance.setLastResetYear(LocalDate.now().getYear());
			}

			trans.begin();
			em.persist(leaveBalance);
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
	public void update(LeaveBalance leaveBalance) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.merge(leaveBalance);
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
}

