package com.srms.service;

import com.srms.model.Student;
import com.srms.repository.StudentRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class StudentService {

    private final StudentRepository repo;

    public StudentService() { this.repo = new StudentRepository(); }

    public void addStudent(String rollNo, String fullName, String email,
                           String phone, String dept, int semester) throws SQLException {
        if (repo.findByRollNo(rollNo).isPresent())
            throw new IllegalArgumentException("Roll No already exists: " + rollNo);
        repo.save(new Student(rollNo, fullName, email, phone, dept, semester));
        System.out.println("✔ Student added: " + rollNo);
    }

    public void updateStudent(int id, String fullName, String email,
                              String phone, String dept, int semester) throws SQLException {
        Student s = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + id));
        s.setFullName  (fullName);
        s.setEmail     (email);
        s.setPhone     (phone);
        s.setDepartment(dept);
        s.setSemester  (semester);
        repo.update(s);
        System.out.println("✔ Student updated.");
    }

    public void deleteStudent(int id) throws SQLException {
        repo.delete(id);
        System.out.println("✔ Student deleted.");
    }

    public Optional<Student> findById(int id)          throws SQLException { return repo.findById(id); }
    public Optional<Student> findByRollNo(String rn)   throws SQLException { return repo.findByRollNo(rn); }
    public List<Student>     getAllStudents()           throws SQLException { return repo.findAll(); }
    public List<Student>     searchStudents(String kw) throws SQLException { return repo.search(kw); }
    public List<Student>     getByDepartment(String d) throws SQLException { return repo.findByDepartment(d); }
}
