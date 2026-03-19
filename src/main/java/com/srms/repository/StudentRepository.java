package com.srms.repository;

import com.srms.config.DBConnection;
import com.srms.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepository implements Repository<Student, Integer> {

    @Override
    public void save(Student s) throws SQLException {
        String sql = "INSERT INTO students (roll_no, full_name, email, phone, department, semester) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getRollNo());
            ps.setString(2, s.getFullName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getDepartment());
            ps.setInt   (6, s.getSemester());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) s.setId(keys.getInt(1));
            }
        }
    }

    @Override
    public void update(Student s) throws SQLException {
        String sql = "UPDATE students SET full_name=?, email=?, phone=?, department=?, semester=? " +
                     "WHERE id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, s.getFullName());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getPhone());
            ps.setString(4, s.getDepartment());
            ps.setInt   (5, s.getSemester());
            ps.setInt   (6, s.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement("DELETE FROM students WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<Student> findById(Integer id) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement("SELECT * FROM students WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
        }
        return Optional.empty();
    }

    public Optional<Student> findByRollNo(String rollNo) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement("SELECT * FROM students WHERE roll_no=?")) {
            ps.setString(1, rollNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
        }
        return Optional.empty();
    }

    @Override
    public List<Student> findAll() throws SQLException {
        List<Student> list = new ArrayList<>();
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM students ORDER BY roll_no")) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Student> findByDepartment(String dept) throws SQLException {
        List<Student> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement("SELECT * FROM students WHERE department=? ORDER BY roll_no")) {
            ps.setString(1, dept);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Student> search(String keyword) throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE full_name LIKE ? OR roll_no LIKE ? ORDER BY roll_no";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            String k = "%" + keyword + "%";
            ps.setString(1, k);
            ps.setString(2, k);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private Student map(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId        (rs.getInt   ("id"));
        s.setRollNo    (rs.getString("roll_no"));
        s.setFullName  (rs.getString("full_name"));
        s.setEmail     (rs.getString("email"));
        s.setPhone     (rs.getString("phone"));
        s.setDepartment(rs.getString("department"));
        s.setSemester  (rs.getInt   ("semester"));
        return s;
    }
}
