package com.srms.service;

import com.srms.model.Course;
import com.srms.repository.CourseRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CourseService {

    private final CourseRepository repo;

    public CourseService() { this.repo = new CourseRepository(); }

    public void addCourse(String code, String name, int credits, int facultyId) throws SQLException {
        repo.save(new Course(code, name, credits, facultyId));
        System.out.println("✔ Course added: " + code);
    }

    public void updateCourse(int id, String code, String name, int credits, int facultyId) throws SQLException {
        Course c = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + id));
        c.setCode     (code);
        c.setName     (name);
        c.setCredits  (credits);
        c.setFacultyId(facultyId);
        repo.update(c);
        System.out.println("✔ Course updated.");
    }

    public void deleteCourse(int id) throws SQLException {
        repo.delete(id);
        System.out.println("✔ Course deleted.");
    }

    public Optional<Course> findById(int id)    throws SQLException { return repo.findById(id); }
    public List<Course>     getAllCourses()      throws SQLException { return repo.findAll(); }
}
