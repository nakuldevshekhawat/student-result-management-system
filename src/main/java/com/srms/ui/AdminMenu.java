package com.srms.ui;

import com.srms.model.*;
import com.srms.service.*;
import com.srms.util.Utils;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Console menu for ADMIN role.
 * Provides: student mgmt, course mgmt, grade mgmt, reports, user mgmt.
 */
public class AdminMenu {

    private final Scanner       sc;
    private final AuthService   authSvc;
    private final StudentService studentSvc  = new StudentService();
    private final CourseService  courseSvc   = new CourseService();
    private final GradeService   gradeSvc    = new GradeService();

    public AdminMenu(Scanner sc, AuthService authSvc) {
        this.sc      = sc;
        this.authSvc = authSvc;
    }

    public void show() {
        boolean active = true;
        while (active) {
            Utils.printBanner("ADMIN MENU");
            System.out.println("  [1]  Manage Students");
            System.out.println("  [2]  Manage Courses");
            System.out.println("  [3]  Manage Grades");
            System.out.println("  [4]  Reports");
            System.out.println("  [5]  Manage Users");
            System.out.println("  [0]  Logout");
            System.out.print ("  > ");

            switch (sc.nextLine().trim()) {
                case "1" -> studentsMenu();
                case "2" -> coursesMenu();
                case "3" -> gradesMenu();
                case "4" -> reportsMenu();
                case "5" -> usersMenu();
                case "0" -> { authSvc.logout(); active = false; }
                default  -> System.out.println("  Invalid choice.");
            }
        }
    }

    // ═══════════════════ STUDENTS ══════════════════════════════════

    private void studentsMenu() {
        boolean active = true;
        while (active) {
            Utils.printBanner("STUDENTS");
            System.out.println("  [1] List all    [2] Search    [3] Add");
            System.out.println("  [4] Edit        [5] Delete    [0] Back");
            System.out.print ("  > ");
            switch (sc.nextLine().trim()) {
                case "1" -> listStudents();
                case "2" -> searchStudents();
                case "3" -> addStudent();
                case "4" -> editStudent();
                case "5" -> deleteStudent();
                case "0" -> active = false;
            }
        }
    }

    private void listStudents() {
        try {
            List<Student> list = studentSvc.getAllStudents();
            if (list.isEmpty()) { System.out.println("  No students found."); return; }
            Utils.printLine();
            System.out.printf("  %-5s %-12s %-25s %-20s %s%n", "ID", "Roll No", "Name", "Department", "Sem");
            Utils.printLine();
            for (Student s : list)
                System.out.printf("  %-5d %-12s %-25s %-20s %d%n",
                        s.getId(), s.getRollNo(), s.getFullName(), s.getDepartment(), s.getSemester());
            Utils.printLine();
        } catch (SQLException e) { err(e); }
    }

    private void searchStudents() {
        System.out.print("  Keyword (name / roll no): ");
        String kw = sc.nextLine().trim();
        try {
            List<Student> list = studentSvc.searchStudents(kw);
            if (list.isEmpty()) System.out.println("  No results.");
            else list.forEach(s -> System.out.println("  " + s));
        } catch (SQLException e) { err(e); }
    }

    private void addStudent() {
        try {
            System.out.print("  Roll No    : "); String rollNo = sc.nextLine().trim();
            System.out.print("  Full Name  : "); String name   = sc.nextLine().trim();
            System.out.print("  Email      : "); String email  = sc.nextLine().trim();
            System.out.print("  Phone      : "); String phone  = sc.nextLine().trim();
            System.out.print("  Department : "); String dept   = sc.nextLine().trim();
            System.out.print("  Semester   : "); int sem       = Integer.parseInt(sc.nextLine().trim());
            studentSvc.addStudent(rollNo, name, email, phone, dept, sem);
        } catch (Exception e) { System.out.println("  ✖ " + e.getMessage()); }
    }

    private void editStudent() {
        System.out.print("  Student ID to edit: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            Optional<Student> opt = studentSvc.findById(id);
            if (opt.isEmpty()) { System.out.println("  Not found."); return; }
            Student s = opt.get();
            System.out.println("  Leave blank to keep current value.");
            System.out.printf ("  Full Name  [%s]: ", s.getFullName());
            String name = sc.nextLine().trim(); if (!name.isEmpty()) s.setFullName(name);
            System.out.printf ("  Email      [%s]: ", s.getEmail());
            String email = sc.nextLine().trim(); if (!email.isEmpty()) s.setEmail(email);
            System.out.printf ("  Phone      [%s]: ", s.getPhone());
            String phone = sc.nextLine().trim(); if (!phone.isEmpty()) s.setPhone(phone);
            System.out.printf ("  Department [%s]: ", s.getDepartment());
            String dept = sc.nextLine().trim(); if (!dept.isEmpty()) s.setDepartment(dept);
            System.out.printf ("  Semester   [%d]: ", s.getSemester());
            String sem = sc.nextLine().trim();
            studentSvc.updateStudent(s.getId(), s.getFullName(), s.getEmail(),
                    s.getPhone(), s.getDepartment(),
                    sem.isEmpty() ? s.getSemester() : Integer.parseInt(sem));
        } catch (Exception e) { System.out.println("  ✖ " + e.getMessage()); }
    }

    private void deleteStudent() {
        System.out.print("  Student ID to delete: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            System.out.print("  Confirm delete? (y/n): ");
            if ("y".equalsIgnoreCase(sc.nextLine().trim())) studentSvc.deleteStudent(id);
        } catch (Exception e) { System.out.println("  ✖ " + e.getMessage()); }
    }

    // ═══════════════════ COURSES ═══════════════════════════════════

    private void coursesMenu() {
        boolean active = true;
        while (active) {
            Utils.printBanner("COURSES");
            System.out.println("  [1] List all  [2] Add  [3] Edit  [4] Delete  [0] Back");
            System.out.print ("  > ");
            switch (sc.nextLine().trim()) {
                case "1" -> listCourses();
                case "2" -> addCourse();
                case "3" -> editCourse();
                case "4" -> deleteCourse();
                case "0" -> active = false;
            }
        }
    }

    private void listCourses() {
        try {
            List<Course> list = courseSvc.getAllCourses();
            Utils.printLine();
            System.out.printf("  %-5s %-8s %-30s %-8s %-20s%n", "ID", "Code", "Name", "Credits", "Faculty");
            Utils.printLine();
            for (Course c : list)
                System.out.printf("  %-5d %-8s %-30s %-8d %-20s%n",
                        c.getId(), c.getCode(), c.getName(), c.getCredits(),
                        c.getFacultyName() != null ? c.getFacultyName() : "-");
            Utils.printLine();
        } catch (SQLException e) { err(e); }
    }

    private void addCourse() {
        try {
            System.out.print("  Code      : "); String code    = sc.nextLine().trim();
            System.out.print("  Name      : "); String name    = sc.nextLine().trim();
            System.out.print("  Credits   : "); int    credits = Integer.parseInt(sc.nextLine().trim());
            System.out.print("  Faculty ID: "); int    fid     = Integer.parseInt(sc.nextLine().trim());
            courseSvc.addCourse(code, name, credits, fid);
        } catch (Exception e) { System.out.println("  ✖ " + e.getMessage()); }
    }

    private void editCourse() {
        System.out.print("  Course ID to edit: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            Optional<Course> opt = courseSvc.findById(id);
            if (opt.isEmpty()) { System.out.println("  Not found."); return; }
            Course c = opt.get();
            System.out.printf("  Code    [%s]: ", c.getCode());
            String code = sc.nextLine().trim(); if (!code.isEmpty()) c.setCode(code);
            System.out.printf("  Name    [%s]: ", c.getName());
            String name = sc.nextLine().trim(); if (!name.isEmpty()) c.setName(name);
            System.out.printf("  Credits [%d]: ", c.getCredits());
            String cred = sc.nextLine().trim();
            courseSvc.updateCourse(c.getId(), c.getCode(), c.getName(),
                    cred.isEmpty() ? c.getCredits() : Integer.parseInt(cred), c.getFacultyId());
        } catch (Exception e) { System.out.println("  ✖ " + e.getMessage()); }
    }

    private void deleteCourse() {
        System.out.print("  Course ID to delete: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            System.out.print("  Confirm delete? (y/n): ");
            if ("y".equalsIgnoreCase(sc.nextLine().trim())) courseSvc.deleteCourse(id);
        } catch (Exception e) { System.out.println("  ✖ " + e.getMessage()); }
    }

    // ═══════════════════ GRADES ════════════════════════════════════

    private void gradesMenu() {
        boolean active = true;
        while (active) {
            Utils.printBanner("GRADES");
            System.out.println("  [1] View student grades  [2] Record/update grade  [0] Back");
            System.out.print ("  > ");
            switch (sc.nextLine().trim()) {
                case "1" -> viewStudentGrades();
                case "2" -> recordGrade();
                case "0" -> active = false;
            }
        }
    }

    private void viewStudentGrades() {
        System.out.print("  Student ID: ");
        try {
            int sid = Integer.parseInt(sc.nextLine().trim());
            System.out.print("  Semester (0 = all): ");
            int sem = Integer.parseInt(sc.nextLine().trim());
            if (sem == 0) gradeSvc.getStudentGrades(sid).forEach(g -> System.out.println("  " + g));
            else          gradeSvc.printResultCard(sid, sem);
        } catch (Exception e) { System.out.println("  ✖ " + e.getMessage()); }
    }

    private void recordGrade() {
        try {
            System.out.print("  Student ID  : "); int    sid   = Integer.parseInt(sc.nextLine().trim());
            System.out.print("  Course ID   : "); int    cid   = Integer.parseInt(sc.nextLine().trim());
            System.out.print("  Semester    : "); int    sem   = Integer.parseInt(sc.nextLine().trim());
            System.out.print("  Marks (0-100): "); double marks = Double.parseDouble(sc.nextLine().trim());
            gradeSvc.recordGrade(sid, cid, marks, sem, authSvc.getCurrentUser().getId());
        } catch (Exception e) { System.out.println("  ✖ " + e.getMessage()); }
    }

    // ═══════════════════ REPORTS ═══════════════════════════════════

    private void reportsMenu() {
        boolean active = true;
        while (active) {
            Utils.printBanner("REPORTS");
            System.out.println("  [1] Result card (student + semester)");
            System.out.println("  [2] Course report (course + semester)");
            System.out.println("  [3] Export student result to CSV");
            System.out.println("  [0] Back");
            System.out.print ("  > ");
            switch (sc.nextLine().trim()) {
                case "1" -> {
                    System.out.print("  Student ID: "); int s = intInput();
                    System.out.print("  Semester  : "); int sem = intInput();
                    try { gradeSvc.printResultCard(s, sem); } catch (SQLException e) { err(e); }
                }
                case "2" -> {
                    System.out.print("  Course ID: "); int c = intInput();
                    System.out.print("  Semester : "); int sem = intInput();
                    try { gradeSvc.printCourseReport(c, sem); } catch (SQLException e) { err(e); }
                }
                case "3" -> {
                    System.out.print("  Student ID: "); int s = intInput();
                    try {
                        String csv = gradeSvc.exportStudentResultCSV(s);
                        String filename = "student_" + s + "_result.csv";
                        java.nio.file.Files.writeString(java.nio.file.Path.of(filename), csv);
                        System.out.println("  ✔ Exported to " + filename);
                    } catch (Exception e) { err(e); }
                }
                case "0" -> active = false;
            }
        }
    }

    // ═══════════════════ USERS ═════════════════════════════════════

    private void usersMenu() {
        boolean active = true;
        while (active) {
            Utils.printBanner("USER MANAGEMENT");
            System.out.println("  [1] List users  [2] Add user  [3] Delete user  [4] Change password  [0] Back");
            System.out.print ("  > ");
            switch (sc.nextLine().trim()) {
                case "1" -> {
                    try {
                        authSvc.getAllUsers().forEach(u -> System.out.println("  " + u));
                    } catch (SQLException e) { err(e); }
                }
                case "2" -> {
                    try {
                        System.out.print("  Username  : "); String un   = sc.nextLine().trim();
                        System.out.print("  Password  : "); String pw   = sc.nextLine().trim();
                        System.out.print("  Role (ADMIN/FACULTY): "); String role = sc.nextLine().trim();
                        System.out.print("  Full Name : "); String fn   = sc.nextLine().trim();
                        System.out.print("  Email     : "); String em   = sc.nextLine().trim();
                        authSvc.addUser(un, pw, role, fn, em);
                        System.out.println("  ✔ User created.");
                    } catch (Exception e) { System.out.println("  ✖ " + e.getMessage()); }
                }
                case "3" -> {
                    System.out.print("  User ID to delete: ");
                    try { authSvc.deleteUser(intInput()); System.out.println("  ✔ User deleted."); }
                    catch (SQLException e) { err(e); }
                }
                case "4" -> {
                    System.out.print("  User ID        : "); int id = intInput();
                    System.out.print("  New password   : "); String pw = sc.nextLine().trim();
                    try { authSvc.changePassword(id, pw); System.out.println("  ✔ Password updated."); }
                    catch (SQLException e) { err(e); }
                }
                case "0" -> active = false;
            }
        }
    }

    // ── helpers ──────────────────────────────────────────────────────

    private int intInput() {
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private void err(Exception e) { System.err.println("  DB Error: " + e.getMessage()); }
}
