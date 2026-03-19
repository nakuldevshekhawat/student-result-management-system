package com.srms.model;

public class Course {
    private int    id;
    private String code;
    private String name;
    private int    credits;
    private int    facultyId;
    private String facultyName; // joined field

    public Course() {}

    public Course(String code, String name, int credits, int facultyId) {
        this.code      = code;
        this.name      = name;
        this.credits   = credits;
        this.facultyId = facultyId;
    }

    public int    getId()              { return id; }
    public void   setId(int id)        { this.id = id; }

    public String getCode()            { return code; }
    public void   setCode(String v)    { this.code = v; }

    public String getName()            { return name; }
    public void   setName(String v)    { this.name = v; }

    public int    getCredits()         { return credits; }
    public void   setCredits(int v)    { this.credits = v; }

    public int    getFacultyId()       { return facultyId; }
    public void   setFacultyId(int v)  { this.facultyId = v; }

    public String getFacultyName()     { return facultyName; }
    public void   setFacultyName(String v) { this.facultyName = v; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%d credits)", code, name, credits);
    }
}
