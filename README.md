# Student Result Management System

![Build](https://github.com/YOUR_USERNAME/student-result-management-system/actions/workflows/build.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-11+-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)
![License](https://img.shields.io/badge/license-MIT-green)

**Java + MySQL + JDBC | OOP | SOLID | Role-Based Access**

---

## Project Structure

```
StudentResultMS/
├── sql/
│   └── schema.sql                          ← Run this first in MySQL
├── lib/
│   └── mysql-connector-j.jar               ← Download & place here
├── src/main/java/com/srms/
│   ├── config/
│   │   └── DBConnection.java               ← Singleton DB connection
│   ├── model/
│   │   ├── User.java
│   │   ├── Student.java
│   │   ├── Course.java
│   │   └── Grade.java
│   ├── repository/
│   │   ├── Repository.java                 ← Generic CRUD interface
│   │   ├── UserRepository.java
│   │   ├── StudentRepository.java
│   │   ├── CourseRepository.java
│   │   └── GradeRepository.java
│   ├── service/
│   │   ├── AuthService.java
│   │   ├── StudentService.java
│   │   ├── CourseService.java
│   │   └── GradeService.java
│   └── ui/
│       ├── MainMenu.java                   ← Entry point
│       ├── AdminMenu.java
│       └── FacultyMenu.java
├── build.sh                                ← Linux/Mac build script
├── build.bat                               ← Windows build script
└── README.md
```

---

## Setup

### Step 1 – MySQL Database

```sql
-- In MySQL Workbench or terminal:
source sql/schema.sql
```

This creates the `student_result_ms` database with sample data.

### Step 2 – MySQL JDBC Driver

Download **mysql-connector-j** from:  
https://dev.mysql.com/downloads/connector/j/

Place the `.jar` file inside the `lib/` folder and rename it to:
```
lib/mysql-connector-j.jar
```

### Step 3 – Configure DB Credentials

Open `src/main/java/com/srms/config/DBConnection.java` and update:

```java
private static final String USER     = "root";           // your MySQL username
private static final String PASSWORD = "your_password";  // your MySQL password
```

### Step 4 – Build & Run

**Linux / Mac:**
```bash
chmod +x build.sh
./build.sh run
```

**Windows:**
```bat
build.bat run
```

---

## Default Login Credentials

| Username | Password  | Role    |
|----------|-----------|---------|
| admin    | admin123  | ADMIN   |
| faculty1 | admin123  | FACULTY |

> ⚠️ Change these passwords after first login.

---

## Features

### Admin
- **Students** – Add, edit, delete, list, search
- **Courses**  – Add, edit, delete, list
- **Grades**   – Record / update grades for any student
- **Reports**  – Result card, course report, CSV export
- **Users**    – Add/delete faculty & admin accounts, reset passwords

### Faculty
- **View Students** – Browse and search all students
- **Record Grades** – Enter or update marks for any student/course
- **Result Card**   – View per-student semester report
- **Course Report** – View all grades for a course with statistics
- **Export CSV**    – Export a student's full result to CSV
- **Change Password** – Update own password

---

## Architecture

```
UI Layer       →  AdminMenu / FacultyMenu / MainMenu
                        ↓
Service Layer  →  AuthService / StudentService / CourseService / GradeService
                        ↓
Repository     →  StudentRepository / GradeRepository / CourseRepository / UserRepository
                        ↓
Database       →  MySQL (via JDBC)
```

### OOP Principles Applied
- **Encapsulation** – All model fields private with getters/setters
- **Inheritance**   – Concrete repositories implement `Repository<T, ID>` interface
- **Polymorphism**  – `Repository` interface used throughout service layer

### SOLID Principles Applied
- **S** – Each class has one responsibility (e.g., `GradeService` only handles grade logic)
- **O** – New role menus can be added without changing existing menus
- **L** – Any `Repository<T,ID>` implementation is interchangeable
- **I** – Thin `Repository<T,ID>` interface, not bloated with extra methods
- **D** – Services depend on repository abstractions, not concrete classes

---

## Grade Scale

| Marks  | Grade |
|--------|-------|
| 90–100 | A+    |
| 80–89  | A     |
| 75–79  | B+    |
| 65–74  | B     |
| 55–64  | C+    |
| 50–54  | C     |
| 40–49  | D     |
| < 40   | F     |
