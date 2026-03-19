package com.srms.model;

public class Grade {
    private int    id;
    private int    studentId;
    private int    courseId;
    private double marks;
    private String grade;
    private int    semester;
    private int    recordedBy;

    // Joined fields for display
    private String studentName;
    private String rollNo;
    private String courseName;
    private String courseCode;

    public Grade() {}

    public Grade(int studentId, int courseId, double marks, int semester, int recordedBy) {
        this.studentId  = studentId;
        this.courseId   = courseId;
        this.marks      = marks;
        this.semester   = semester;
        this.recordedBy = recordedBy;
        this.grade      = calculateGrade(marks);
    }

    /** Convert numeric marks to letter grade. */
    public static String calculateGrade(double marks) {
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 75) return "B+";
        if (marks >= 65) return "B";
        if (marks >= 55) return "C+";
        if (marks >= 50) return "C";
        if (marks >= 40) return "D";
        return "F";
    }

    public int    getId()               { return id; }
    public void   setId(int id)         { this.id = id; }

    public int    getStudentId()        { return studentId; }
    public void   setStudentId(int v)   { this.studentId = v; }

    public int    getCourseId()         { return courseId; }
    public void   setCourseId(int v)    { this.courseId = v; }

    public double getMarks()            { return marks; }
    public void   setMarks(double v)    { this.marks = v; this.grade = calculateGrade(v); }

    public String getGrade()            { return grade; }
    public void   setGrade(String v)    { this.grade = v; }

    public int    getSemester()         { return semester; }
    public void   setSemester(int v)    { this.semester = v; }

    public int    getRecordedBy()       { return recordedBy; }
    public void   setRecordedBy(int v)  { this.recordedBy = v; }

    public String getStudentName()      { return studentName; }
    public void   setStudentName(String v) { this.studentName = v; }

    public String getRollNo()           { return rollNo; }
    public void   setRollNo(String v)   { this.rollNo = v; }

    public String getCourseName()       { return courseName; }
    public void   setCourseName(String v) { this.courseName = v; }

    public String getCourseCode()       { return courseCode; }
    public void   setCourseCode(String v) { this.courseCode = v; }

    @Override
    public String toString() {
        return String.format("%-12s %-30s %6.2f  %-3s", courseCode, courseName, marks, grade);
    }
}
