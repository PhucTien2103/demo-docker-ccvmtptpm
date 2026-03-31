package nhom13.vn.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import nhom13.vn.config.JPAConfig;
import nhom13.vn.dao.IUserDao;
import nhom13.vn.entity.User;

import java.util.List;


public class UserDaoImpl implements IUserDao {

    @Override
    public User findByUsernameAndPassword(String username, String password) {

        EntityManager em = JPAConfig.getEntityManager();
        User user = null;

        try {

            String jpql = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password AND u.status = 1";

            TypedQuery<User> query = em.createQuery(jpql, User.class);

            query.setParameter("username", username);
            query.setParameter("password", password);

            user = query.getSingleResult();

        } catch (Exception e) {
            user = null;
        } finally {
            em.close();
        }

        return user;
    }
    
    @Override
    public void insert(User user) {

        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            em.persist(user);

            trans.commit();

        } catch (Exception e) {

            trans.rollback();
            throw e;

        } finally {

            em.close();
        }
    }
    
    @Override
    public boolean checkExistUsername(String username) {

        EntityManager em = JPAConfig.getEntityManager();

        try {

            String jpql = "SELECT COUNT(u) FROM User u WHERE u.username = :username";

            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("username", username)
                    .getSingleResult();

            return count > 0;

        } finally {

            em.close();
        }
    }
    
    @Override
    public boolean checkExistEmail(String email) {

        EntityManager em = JPAConfig.getEntityManager();

        try {

            String jpql = "SELECT COUNT(u) FROM User u WHERE u.email = :email";

            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("email", email)
                    .getSingleResult();

            return count > 0;

        } finally {

            em.close();
        }
    }
    
    @Override
    public User findByEmail(String email) {

        EntityManager em = JPAConfig.getEntityManager();

        try {

            String jpql = "SELECT u FROM User u WHERE u.email = :email";

            return em.createQuery(jpql, User.class)
                    .setParameter("email", email)
                    .getSingleResult();

        } catch (Exception e) {

            return null;

        } finally {

            em.close();

        }
    }
    
    @Override
    public void update(User user) {

        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {

            trans.begin();

            em.merge(user);

            trans.commit();

        } catch (Exception e) {

            trans.rollback();
            throw e;

        } finally {

            em.close();

        }
    }

    @Override
    public User findById(int id) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> findByRole(String role) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String jpql = "SELECT u FROM User u WHERE u.role = :role AND u.status = 1";
            return em.createQuery(jpql, User.class)
                    .setParameter("role", role)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> findByRoles(List<String> roles) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String jpql = "SELECT u FROM User u WHERE u.role IN :roles AND u.status = 1";
            return em.createQuery(jpql, User.class)
                    .setParameter("roles", roles)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public boolean updatePasswordForSelf(int userId, String oldPassword, String newPassword) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            User user;
            try {
                user = em.createQuery(
                                "SELECT u FROM User u WHERE u.id = :id AND u.password = :oldPw AND u.status = 1",
                                User.class
                        )
                        .setParameter("id", userId)
                        .setParameter("oldPw", oldPassword)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .getSingleResult();
            } catch (NoResultException e) {
                trans.rollback();
                return false;
            }

            user.setPassword(newPassword);

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
    public boolean updatePasswordByAdmin(int targetUserId, String newPassword) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            User user = em.find(User.class, targetUserId, LockModeType.PESSIMISTIC_WRITE);
            if (user == null || user.getStatus() != 1) {
                trans.rollback();
                return false;
            }

            user.setPassword(newPassword);

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