package com.srms.repository;

import com.srms.config.DBConnection;
import com.srms.model.Grade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GradeRepository implements Repository<Grade, Integer> {

    @Override
    public void save(Grade g) throws SQLException {
        String sql = "INSERT INTO grades (student_id, course_id, marks, grade, semester, recorded_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE marks=VALUES(marks), grade=VALUES(grade), recorded_by=VALUES(recorded_by)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt   (1, g.getStudentId());
            ps.setInt   (2, g.getCourseId());
            ps.setDouble(3, g.getMarks());
            ps.setString(4, g.getGrade());
            ps.setInt   (5, g.getSemester());
            ps.setInt   (6, g.getRecordedBy());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) g.setId(keys.getInt(1));
            }
        }
    }

    @Override
    public void update(Grade g) throws SQLException {
        String sql = "UPDATE grades SET marks=?, grade=? WHERE id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setDouble(1, g.getMarks());
            ps.setString(2, g.getGrade());
            ps.setInt   (3, g.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement("DELETE FROM grades WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<Grade> findById(Integer id) throws SQLException {
        String sql = "SELECT g.*, s.roll_no, s.full_name AS student_name, " +
                     "c.code AS course_code, c.name AS course_name " +
                     "FROM grades g " +
                     "JOIN students s ON g.student_id = s.id " +
                     "JOIN courses  c ON g.course_id  = c.id " +
                     "WHERE g.id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
        }
        return Optional.empty();
    }

    @Override
    public List<Grade> findAll() throws SQLException {
        return findWithJoin("1=1", new Object[0]);
    }

    /** All grades for a specific student. */
    public List<Grade> findByStudent(int studentId) throws SQLException {
        return findWithJoin("g.student_id=?", new Object[]{studentId});
    }

    /** All grades for a specific student in a semester. */
    public List<Grade> findByStudentAndSemester(int studentId, int semester) throws SQLException {
        return findWithJoin("g.student_id=? AND g.semester=?", new Object[]{studentId, semester});
    }

    /** All grades for a course in a semester. */
    public List<Grade> findByCourseAndSemester(int courseId, int semester) throws SQLException {
        return findWithJoin("g.course_id=? AND g.semester=?", new Object[]{courseId, semester});
    }

    // ---------------------------------------------------------------- helpers

    private List<Grade> findWithJoin(String where, Object[] params) throws SQLException {
        List<Grade> list = new ArrayList<>();
        String sql = "SELECT g.*, s.roll_no, s.full_name AS student_name, " +
                     "c.code AS course_code, c.name AS course_name " +
                     "FROM grades g " +
                     "JOIN students s ON g.student_id = s.id " +
                     "JOIN courses  c ON g.course_id  = c.id " +
                     "WHERE " + where + " ORDER BY s.roll_no, c.code";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof Integer) ps.setInt(i + 1, (Integer) params[i]);
                else ps.setString(i + 1, params[i].toString());
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private Grade map(ResultSet rs) throws SQLException {
        Grade g = new Grade();
        g.setId         (rs.getInt   ("id"));
        g.setStudentId  (rs.getInt   ("student_id"));
        g.setCourseId   (rs.getInt   ("course_id"));
        g.setMarks      (rs.getDouble("marks"));
        g.setGrade      (rs.getString("grade"));
        g.setSemester   (rs.getInt   ("semester"));
        g.setRecordedBy (rs.getInt   ("recorded_by"));
        // joined
        try { g.setRollNo     (rs.getString("roll_no"));      } catch (SQLException ignored) {}
        try { g.setStudentName(rs.getString("student_name")); } catch (SQLException ignored) {}
        try { g.setCourseCode (rs.getString("course_code"));  } catch (SQLException ignored) {}
        try { g.setCourseName (rs.getString("course_name"));  } catch (SQLException ignored) {}
        return g;
    }
}
