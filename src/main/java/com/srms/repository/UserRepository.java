package com.srms.repository;

import com.srms.config.DBConnection;
import com.srms.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements Repository<User, Integer> {

    @Override
    public void save(User u) throws SQLException {
        String sql = "INSERT INTO users (username, password, role, full_name, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRole());
            ps.setString(4, u.getFullName());
            ps.setString(5, u.getEmail());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) u.setId(keys.getInt(1));
            }
        }
    }

    @Override
    public void update(User u) throws SQLException {
        String sql = "UPDATE users SET full_name=?, email=?, role=? WHERE id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getRole());
            ps.setInt   (4, u.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement("DELETE FROM users WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<User> findById(Integer id) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement("SELECT * FROM users WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> list = new ArrayList<>();
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM users ORDER BY role, full_name")) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    /** Authenticate by username + hashed password. */
    public Optional<User> authenticate(String username, String hashedPassword) throws SQLException {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
        }
        return Optional.empty();
    }

    public void changePassword(int userId, String newHashedPassword) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement("UPDATE users SET password=? WHERE id=?")) {
            ps.setString(1, newHashedPassword);
            ps.setInt   (2, userId);
            ps.executeUpdate();
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId      (rs.getInt   ("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setRole    (rs.getString("role"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail   (rs.getString("email"));
        return u;
    }
}
