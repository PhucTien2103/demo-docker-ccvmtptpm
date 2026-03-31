package nhom13.vn.service.impl;

import nhom13.vn.dao.IUserDao;
import nhom13.vn.dao.impl.UserDaoImpl;
import nhom13.vn.service.IUserService;
import nhom13.vn.entity.User;
import nhom13.vn.service.ILeaveBalanceService;

import java.util.List;

public class UserServiceImpl implements IUserService {

    IUserDao userDao = new UserDaoImpl();
    ILeaveBalanceService leaveBalanceService = LeaveBalanceServiceImpl.getInstance();

    @Override
    public User login(String username, String password) {

        return userDao.findByUsernameAndPassword(username, password);

    }

    @Override
    public int register(String username, String password, String email, String role) {

        if (userDao.checkExistUsername(username)) {
            return 1;
        }

        if (userDao.checkExistEmail(email)) {
            return 2;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole(role);
        user.setStatus(1);

        userDao.insert(user);
        leaveBalanceService.ensureDefaultForUser(user);

        return 0;
    }

    @Override
    public User findByEmail(String email) {

        return userDao.findByEmail(email);

    }

    @Override
    public void update(User user) {

        userDao.update(user);

    }

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findByRole(String role) {
        return userDao.findByRole(role);
    }

    @Override
    public List<User> findByRoles(List<String> roles) {
        return userDao.findByRoles(roles);
    }

    @Override
    public void insert(User user) {

        if (userDao.checkExistUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userDao.checkExistEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        userDao.insert(user);
        leaveBalanceService.ensureDefaultForUser(user);
    }
    
    @Override
    public boolean changePasswordForSelf(User currentUser, String oldPassword, String newPassword) {
        if (currentUser == null || currentUser.getId() <= 0) {
            return false;
        }
        if (oldPassword == null || newPassword == null) {
            return false;
        }

        String oldTrimmed = oldPassword.trim();
        String newTrimmed = newPassword.trim();

        if (oldTrimmed.isEmpty() || newTrimmed.isEmpty()) {
            return false;
        }

        // Không cho dùng lại mật khẩu cũ
        if (newTrimmed.equals(oldTrimmed)) {
            return false;
        }

        if (!isValidNewPassword(newTrimmed)) {
            return false;
        }

        // Check lại DB để tránh dùng password trong session bị cũ
        User fresh = userDao.findById(currentUser.getId());
        if (fresh == null || fresh.getStatus() != 1) {
            return false;
        }

        // Nhập đúng mật khẩu cũ
        if (!oldTrimmed.equals(fresh.getPassword())) {
            return false;
        }

        boolean updated = userDao.updatePasswordForSelf(currentUser.getId(), oldTrimmed, newTrimmed);
        if (updated) {
            currentUser.setPassword(newTrimmed);
        }
        return updated;
    }

    @Override
    public boolean changePasswordAsAdmin(User adminUser, int targetUserId, String newPassword) {
        if (adminUser == null || !"SUPER_ADMIN".equals(adminUser.getRole())) {
            return false;
        }
        if (targetUserId <= 0 || newPassword == null) {
            return false;
        }

        String newTrimmed = newPassword.trim();
        if (newTrimmed.isEmpty() || !isValidNewPassword(newTrimmed)) {
            return false;
        }

        User target = userDao.findById(targetUserId);
        if (target == null || target.getStatus() != 1) {
            return false;
        }

        // Admin chỉ được đổi mật khẩu cho employee + manager
        if (!"EMPLOYEE".equals(target.getRole()) && !"MANAGER".equals(target.getRole())) {
            return false;
        }

        // Không cho dùng lại mật khẩu cũ
        if (newTrimmed.equals(target.getPassword())) {
            return false;
        }

        return userDao.updatePasswordByAdmin(targetUserId, newTrimmed);
    }

    private boolean isValidNewPassword(String password) {
        // Validate mật khẩu mới: >= 8 ký tự, có hoa/thường/số, không có khoảng trắng
        if (password == null) {
            return false;
        }
        if (password.length() < 8) {
            return false;
        }
        if (password.contains(" ")) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
        }
        return hasUpper && hasLower && hasDigit;
    }

}