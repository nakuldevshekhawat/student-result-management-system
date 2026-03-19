package com.srms.service;

import com.srms.model.User;
import com.srms.repository.UserRepository;
import com.srms.util.Utils;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Handles authentication and user management.
 * Keeps the currently logged-in user in session.
 */
public class AuthService {

    private final UserRepository userRepo;
    private User currentUser;

    public AuthService() {
        this.userRepo = new UserRepository();
    }

    /** Attempt login. Returns true on success. */
    public boolean login(String username, String password) throws SQLException {
        String hash = Utils.hashPassword(password);
        Optional<User> user = userRepo.authenticate(username, hash);
        if (user.isPresent()) {
            currentUser = user.get();
            return true;
        }
        return false;
    }

    public void logout() { currentUser = null; }

    public User getCurrentUser()       { return currentUser; }
    public boolean isLoggedIn()        { return currentUser != null; }
    public boolean isAdmin()           { return isLoggedIn() && currentUser.isAdmin(); }
    public boolean isFaculty()         { return isLoggedIn() && currentUser.isFaculty(); }

    // ── User management (admin only) ──────────────────────────────────

    public void addUser(String username, String password, String role,
                        String fullName, String email) throws SQLException {
        User u = new User();
        u.setUsername(username);
        u.setPassword(Utils.hashPassword(password));
        u.setRole    (role.toUpperCase());
        u.setFullName(fullName);
        u.setEmail   (email);
        userRepo.save(u);
    }

    public void changePassword(int userId, String newPassword) throws SQLException {
        userRepo.changePassword(userId, Utils.hashPassword(newPassword));
    }

    public void updateUser(User u) throws SQLException { userRepo.update(u); }

    public void deleteUser(int id) throws SQLException { userRepo.delete(id); }

    public List<User> getAllUsers() throws SQLException { return userRepo.findAll(); }

    public Optional<User> getUserById(int id) throws SQLException {
        return userRepo.findById(id);
    }
}
