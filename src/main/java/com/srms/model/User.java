package com.srms.model;

public class User {
    private int    id;
    private String username;
    private String password;  // stored as hash
    private String role;      // "ADMIN" or "FACULTY"
    private String fullName;
    private String email;

    public User() {}

    public User(int id, String username, String role, String fullName, String email) {
        this.id       = id;
        this.username = username;
        this.role     = role;
        this.fullName = fullName;
        this.email    = email;
    }

    // Getters & setters
    public int    getId()          { return id; }
    public void   setId(int id)    { this.id = id; }

    public String getUsername()              { return username; }
    public void   setUsername(String v)      { this.username = v; }

    public String getPassword()              { return password; }
    public void   setPassword(String v)      { this.password = v; }

    public String getRole()                  { return role; }
    public void   setRole(String v)          { this.role = v; }

    public String getFullName()              { return fullName; }
    public void   setFullName(String v)      { this.fullName = v; }

    public String getEmail()                 { return email; }
    public void   setEmail(String v)         { this.email = v; }

    public boolean isAdmin()   { return "ADMIN".equalsIgnoreCase(role); }
    public boolean isFaculty() { return "FACULTY".equalsIgnoreCase(role); }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s) - %s", id, fullName, role, username);
    }
}
