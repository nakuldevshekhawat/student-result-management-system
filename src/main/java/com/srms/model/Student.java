package com.srms.model;

public class Student {
    private int    id;
    private String rollNo;
    private String fullName;
    private String email;
    private String phone;
    private String department;
    private int    semester;

    public Student() {}

    public Student(String rollNo, String fullName, String email,
                   String phone, String department, int semester) {
        this.rollNo     = rollNo;
        this.fullName   = fullName;
        this.email      = email;
        this.phone      = phone;
        this.department = department;
        this.semester   = semester;
    }

    // Getters & setters
    public int    getId()              { return id; }
    public void   setId(int id)        { this.id = id; }

    public String getRollNo()          { return rollNo; }
    public void   setRollNo(String v)  { this.rollNo = v; }

    public String getFullName()        { return fullName; }
    public void   setFullName(String v){ this.fullName = v; }

    public String getEmail()           { return email; }
    public void   setEmail(String v)   { this.email = v; }

    public String getPhone()           { return phone; }
    public void   setPhone(String v)   { this.phone = v; }

    public String getDepartment()      { return department; }
    public void   setDepartment(String v) { this.department = v; }

    public int    getSemester()        { return semester; }
    public void   setSemester(int v)   { this.semester = v; }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | Sem %d", rollNo, fullName, department, semester);
    }
}
