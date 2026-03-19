package com.srms.repository;

import com.srms.config.DBConnection;
import com.srms.model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseRepository implements Repository<Course, Integer> {

    @Override
    public void save(Course c) throws SQLException {
        String sql = "INSERT INTO courses (code, name, credits, faculty_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getCode());
            ps.setString(2, c.getName());
            ps.setInt   (3, c.getCredits());
            if (c.getFacultyId() > 0) ps.setInt(4, c.getFacultyId());
            else                      ps.setNull(4, Types.INTEGER);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) c.setId(keys.getInt(1));
            }
        }
    }

    @Override
    public void update(Course c) throws SQLException {
        String sql = "UPDATE courses SET code=?, name=?, credits=?, faculty_id=? WHERE id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, c.getCode());
            ps.setString(2, c.getName());
            ps.setInt   (3, c.getCredits());
            if (c.getFacultyId() > 0) ps.setInt(4, c.getFacultyId());
            else                      ps.setNull(4, Types.INTEGER);
            ps.setInt(5, c.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement("DELETE FROM courses WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<Course> findById(Integer id) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement("SELECT c.*, u.full_name AS faculty_name FROM courses c " +
                                  "LEFT JOIN users u ON c.faculty_id=u.id WHERE c.id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
        }
        return Optional.empty();
    }

    @Override
    public List<Course> findAll() throws SQLException {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT c.*, u.full_name AS faculty_name FROM courses c " +
                     "LEFT JOIN users u ON c.faculty_id=u.id ORDER BY c.code";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private Course map(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setId         (rs.getInt   ("id"));
        c.setCode       (rs.getString("code"));
        c.setName       (rs.getString("name"));
        c.setCredits    (rs.getInt   ("credits"));
        c.setFacultyId  (rs.getInt   ("faculty_id"));
        try { c.setFacultyName(rs.getString("faculty_name")); } catch (SQLException ignored) {}
        return c;
    }
}
