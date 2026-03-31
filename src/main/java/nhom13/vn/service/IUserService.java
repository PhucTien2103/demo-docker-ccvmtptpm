package nhom13.vn.service;

import nhom13.vn.entity.User;

import java.util.List;

public interface IUserService {

    User login(String username, String password);
    
    int register(String username, String password, String email, String role);

    void insert(User user);

    User findByEmail(String email);

    void update(User user);

    User findById(int id);

    List<User> findByRole(String role);
    List<User> findByRoles(List<String> roles);
    
    boolean changePasswordForSelf(User currentUser, String oldPassword, String newPassword);

    boolean changePasswordAsAdmin(User adminUser, int targetUserId, String newPassword);
}