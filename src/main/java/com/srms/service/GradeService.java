package com.srms.service;

import com.srms.model.Grade;
import com.srms.model.Student;
import com.srms.repository.GradeRepository;
import com.srms.repository.StudentRepository;
import com.srms.util.Utils;

import java.sql.SQLException;
import java.util.*;

public class GradeService {

    private final GradeRepository   gradeRepo;
    private final StudentRepository studentRepo;

    public GradeService() {
        this.gradeRepo   = new GradeRepository();
        this.studentRepo = new StudentRepository();
    }

    /** Record or update a grade. */
    public void recordGrade(int studentId, int courseId, double marks,
                            int semester, int recordedBy) throws SQLException {
        Grade g = new Grade(studentId, courseId, marks, semester, recordedBy);
        gradeRepo.save(g);
        System.out.printf("✔ Grade recorded: %.2f (%s)%n", marks, g.getGrade());
    }

    public List<Grade> getStudentGrades(int studentId) throws SQLException {
        return gradeRepo.findByStudent(studentId);
    }

    public List<Grade> getStudentSemesterGrades(int studentId, int semester) throws SQLException {
        return gradeRepo.findByStudentAndSemester(studentId, semester);
    }

    public List<Grade> getCourseGrades(int courseId, int semester) throws SQLException {
        return gradeRepo.findByCourseAndSemester(courseId, semester);
    }

    // ── Reports ────────────────────────────────────────────────────────

    /** Print a formatted result card for one student, one semester. */
    public void printResultCard(int studentId, int semester) throws SQLException {
        Optional<Student> opt = studentRepo.findById(studentId);
        if (opt.isEmpty()) { System.out.println("Student not found."); return; }
        Student s = opt.get();

        List<Grade> grades = gradeRepo.findByStudentAndSemester(studentId, semester);
        if (grades.isEmpty()) {
            System.out.println("No grades found for semester " + semester);
            return;
        }

        Utils.printBanner("RESULT CARD");
        System.out.printf("  Name       : %s%n", s.getFullName());
        System.out.printf("  Roll No    : %s%n", s.getRollNo());
        System.out.printf("  Department : %s%n", s.getDepartment());
        System.out.printf("  Semester   : %d%n", semester);
        Utils.printLine();
        System.out.printf("  %-10s %-28s %7s  %-4s%n", "Code", "Course", "Marks", "Grade");
        Utils.printLine();

        double totalMarks  = 0;
        int    totalCred   = 0;
        double weightedSum = 0;

        for (Grade g : grades) {
            System.out.printf("  %-10s %-28s %7.2f  %-4s%n",
                    g.getCourseCode(), truncate(g.getCourseName(), 28),
                    g.getMarks(), g.getGrade());
            totalMarks += g.getMarks();
        }

        Utils.printLine();
        System.out.printf("  Average Marks : %.2f%n", totalMarks / grades.size());
        System.out.printf("  Result        : %s%n",
                grades.stream().allMatch(g -> !g.getGrade().equals("F")) ? "PASS" : "FAIL");
        Utils.printLine();
    }

    /** Print all students' grades for a course in a semester. */
    public void printCourseReport(int courseId, int semester) throws SQLException {
        List<Grade> grades = gradeRepo.findByCourseAndSemester(courseId, semester);
        if (grades.isEmpty()) { System.out.println("No records found."); return; }

        Utils.printBanner("COURSE GRADE REPORT  (Sem " + semester + ")");
        System.out.printf("  %-12s %-28s %7s  %-4s%n", "Roll No", "Name", "Marks", "Grade");
        Utils.printLine();

        DoubleSummaryStatistics stats = grades.stream()
                .mapToDouble(Grade::getMarks)
                .summaryStatistics();

        for (Grade g : grades) {
            System.out.printf("  %-12s %-28s %7.2f  %-4s%n",
                    g.getRollNo(), truncate(g.getStudentName(), 28),
                    g.getMarks(), g.getGrade());
        }

        Utils.printLine();
        System.out.printf("  Highest : %.2f  |  Lowest : %.2f  |  Average : %.2f%n",
                stats.getMax(), stats.getMin(), stats.getAverage());
        Utils.printLine();
    }

    /** Export student result to CSV string. */
    public String exportStudentResultCSV(int studentId) throws SQLException {
        List<Grade> grades = gradeRepo.findByStudent(studentId);
        StringBuilder sb = new StringBuilder("CourseCode,CourseName,Semester,Marks,Grade\n");
        for (Grade g : grades) {
            sb.append(String.format("%s,%s,%d,%.2f,%s%n",
                    g.getCourseCode(), g.getCourseName(),
                    g.getSemester(), g.getMarks(), g.getGrade()));
        }
        return sb.toString();
    }

    private String truncate(String s, int max) {
        return (s != null && s.length() > max) ? s.substring(0, max - 1) + "…" : s;
    }
}
