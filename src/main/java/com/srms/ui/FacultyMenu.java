package com.srms.ui;

import com.srms.model.*;
import com.srms.service.*;
import com.srms.util.Utils;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Console menu for FACULTY role.
 * Faculty can: view students, record/update grades, view reports.
 */
public class FacultyMenu {

    private final Scanner        sc;
    private final AuthService    authSvc;
    private final StudentService studentSvc = new StudentService();
    private final CourseService  courseSvc  = new CourseService();
    private final GradeService   gradeSvc   = new GradeService();

    public FacultyMenu(Scanner sc, AuthService authSvc) {
        this.sc      = sc;
        this.authSvc = authSvc;
    }

    public void show() {
        boolean active = true;
        while (active) {
            Utils.printBanner("FACULTY MENU  –  " + authSvc.getCurrentUser().getFullName());
            System.out.println("  [1]  View Students");
            System.out.println("  [2]  Record / Update Grade");
            System.out.println("  [3]  View Student Result Card");
            System.out.println("  [4]  View Course Grade Report");
            System.out.println("  [5]  Export Student Result (CSV)");
            System.out.println("  [6]  Change My Password");
            System.out.println("  [0]  Logout");
            System.out.print ("  > ");

            switch (sc.nextLine().trim()) {
                case "1" -> viewStudents();
                case "2" -> recordGrade();
                case "3" -> resultCard();
                case "4" -> courseReport();
                case "5" -> exportCSV();
                case "6" -> changePassword();
                case "0" -> { authSvc.logout(); active = false; }
                default  -> System.out.println("  Invalid choice.");
            }
        }
    }

    // ── View Students ────────────────────────────────────────────────

    private void viewStudents() {
        boolean active = true;
        while (active) {
            Utils.printBanner("STUDENTS");
            System.out.println("  [1] List all    [2] Search by name/roll    [0] Back");
            System.out.print ("  > ");
            switch (sc.nextLine().trim()) {
                case "1" -> {
                    try {
                        List<Student> list = studentSvc.getAllStudents();
                        if (list.isEmpty()) { System.out.println("  No students found."); break; }
                        Utils.printLine();
                        System.out.printf("  %-5s %-12s %-25s %-20s %s%n",
                                "ID", "Roll No", "Name", "Department", "Sem");
                        Utils.printLine();
                        for (Student s : list)
                            System.out.printf("  %-5d %-12s %-25s %-20s %d%n",
                                    s.getId(), s.getRollNo(), s.getFullName(),
                                    s.getDepartment(), s.getSemester());
                        Utils.printLine();
                    } catch (SQLException e) { err(e); }
                }
                case "2" -> {
                    System.out.print("  Keyword: ");
                    String kw = sc.nextLine().trim();
                    try {
                        studentSvc.searchStudents(kw)
                                  .forEach(s -> System.out.println("  " + s));
                    } catch (SQLException e) { err(e); }
                }
                case "0" -> active = false;
            }
        }
    }

    // ── Record Grade ─────────────────────────────────────────────────

    private void recordGrade() {
        try {
            System.out.println();
            System.out.println("  Available Courses:");
            courseSvc.getAllCourses()
                     .forEach(c -> System.out.printf("    [%d] %s – %s%n",
                             c.getId(), c.getCode(), c.getName()));

            System.out.print("\n  Student ID   : "); int sid   = intInput();
            System.out.print("  Course ID    : "); int cid   = intInput();
            System.out.print("  Semester     : "); int sem   = intInput();
            System.out.print("  Marks (0-100): "); double marks = doubleInput();

            if (marks < 0 || marks > 100) {
                System.out.println("  ✖ Marks must be between 0 and 100.");
                return;
            }

            gradeSvc.recordGrade(sid, cid, marks, sem, authSvc.getCurrentUser().getId());
            System.out.printf("  Calculated grade: %s%n", Grade.calculateGrade(marks));

        } catch (SQLException e) { err(e); }
    }

    // ── Result Card ──────────────────────────────────────────────────

    private void resultCard() {
        System.out.print("  Student ID: "); int sid = intInput();
        System.out.print("  Semester  : "); int sem = intInput();
        try {
            gradeSvc.printResultCard(sid, sem);
        } catch (SQLException e) { err(e); }
    }

    // ── Course Report ─────────────────────────────────────────────────

    private void courseReport() {
        try {
            System.out.println("  Available Courses:");
            courseSvc.getAllCourses()
                     .forEach(c -> System.out.printf("    [%d] %s – %s%n",
                             c.getId(), c.getCode(), c.getName()));

            System.out.print("\n  Course ID: ");  int cid = intInput();
            System.out.print("  Semester  : "); int sem = intInput();
            gradeSvc.printCourseReport(cid, sem);
        } catch (SQLException e) { err(e); }
    }

    // ── Export CSV ───────────────────────────────────────────────────

    private void exportCSV() {
        System.out.print("  Student ID: "); int sid = intInput();
        try {
            String csv      = gradeSvc.exportStudentResultCSV(sid);
            String filename = "student_" + sid + "_result.csv";
            java.nio.file.Files.writeString(java.nio.file.Path.of(filename), csv);
            System.out.println("  ✔ Exported to " + filename);
        } catch (Exception e) { err(e); }
    }

    // ── Change Password ───────────────────────────────────────────────

    private void changePassword() {
        System.out.print("  New password   : "); String pw1 = sc.nextLine().trim();
        System.out.print("  Confirm password: "); String pw2 = sc.nextLine().trim();
        if (!pw1.equals(pw2)) { System.out.println("  ✖ Passwords do not match."); return; }
        try {
            authSvc.changePassword(authSvc.getCurrentUser().getId(), pw1);
            System.out.println("  ✔ Password updated.");
        } catch (SQLException e) { err(e); }
    }

    // ── helpers ──────────────────────────────────────────────────────

    private int intInput() {
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private double doubleInput() {
        try { return Double.parseDouble(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private void err(Exception e) { System.err.println("  DB Error: " + e.getMessage()); }
}
