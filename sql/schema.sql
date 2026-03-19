-- ============================================================
--  Student Result Management System - Database Schema
-- ============================================================

CREATE DATABASE IF NOT EXISTS student_result_ms;
USE student_result_ms;

-- Users table (admin & faculty login)
CREATE TABLE IF NOT EXISTS users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,          -- SHA-256 hashed
    role        ENUM('ADMIN','FACULTY') NOT NULL,
    full_name   VARCHAR(100) NOT NULL,
    email       VARCHAR(100),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Courses table
CREATE TABLE IF NOT EXISTS courses (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(20)  NOT NULL UNIQUE,
    name        VARCHAR(100) NOT NULL,
    credits     INT          NOT NULL DEFAULT 3,
    faculty_id  INT,
    FOREIGN KEY (faculty_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Students table
CREATE TABLE IF NOT EXISTS students (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    roll_no      VARCHAR(20)  NOT NULL UNIQUE,
    full_name    VARCHAR(100) NOT NULL,
    email        VARCHAR(100),
    phone        VARCHAR(15),
    department   VARCHAR(50),
    semester     INT          NOT NULL DEFAULT 1,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Grades table
CREATE TABLE IF NOT EXISTS grades (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    student_id  INT NOT NULL,
    course_id   INT NOT NULL,
    marks       DECIMAL(5,2) NOT NULL,
    grade       VARCHAR(2),
    semester    INT NOT NULL,
    recorded_by INT,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id)  REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id)   REFERENCES courses(id)  ON DELETE CASCADE,
    FOREIGN KEY (recorded_by) REFERENCES users(id)    ON DELETE SET NULL,
    UNIQUE KEY unique_grade (student_id, course_id, semester)
);

-- ============================================================
--  Seed Data
-- ============================================================

-- Default admin (password: admin123)
INSERT IGNORE INTO users (username, password, role, full_name, email) VALUES
('admin',   '240be518fabd2724ddb6f04eeb1da5967448d7e831d1d3caebf4e338caaaf5e5', 'ADMIN',   'System Admin',  'admin@srms.com'),
('faculty1','240be518fabd2724ddb6f04eeb1da5967448d7e831d1d3caebf4e338caaaf5e5', 'FACULTY', 'Dr. Priya Sharma','priya@srms.com');

-- Sample courses
INSERT IGNORE INTO courses (code, name, credits, faculty_id) VALUES
('CS101', 'Introduction to Programming', 4, 2),
('CS201', 'Data Structures',             4, 2),
('MA101', 'Mathematics I',               3, NULL),
('CS301', 'Database Management Systems', 3, 2);

-- Sample students
INSERT IGNORE INTO students (roll_no, full_name, email, phone, department, semester) VALUES
('CS2024001', 'Aarav Sharma',   'aarav@example.com',   '9876543210', 'Computer Science', 3),
('CS2024002', 'Diya Patel',     'diya@example.com',    '9876543211', 'Computer Science', 3),
('CS2024003', 'Rohan Verma',    'rohan@example.com',   '9876543212', 'Computer Science', 3);

-- Sample grades
INSERT IGNORE INTO grades (student_id, course_id, marks, grade, semester, recorded_by) VALUES
(1, 1, 88.5, 'A',  3, 2),
(1, 2, 76.0, 'B+', 3, 2),
(2, 1, 92.0, 'A+', 3, 2),
(2, 2, 85.5, 'A',  3, 2),
(3, 1, 65.0, 'C+', 3, 2),
(3, 2, 71.0, 'B',  3, 2);
