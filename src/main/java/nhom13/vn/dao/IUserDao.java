package nhom13.vn.dao;

import nhom13.vn.entity.User;

import java.util.List;

public interface IUserDao {
    User findByUsernameAndPassword(String username, String password);
    
    void insert(User user);

    boolean checkExistUsername(String username);

    boolean checkExistEmail(String email);
    
    User findByEmail(String email);
    
    void update(User user);

    User findById(int id);

    List<User> findByRole(String role);
    List<User> findByRoles(List<String> roles);
    
    boolean updatePasswordForSelf(int userId, String oldPassword, String newPassword);

    boolean updatePasswordByAdmin(int targetUserId, String newPassword);

}