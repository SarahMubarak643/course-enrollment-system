-- Authorities
INSERT IGNORE INTO authorities (authority_id, authority_name)
VALUES
(1, 'ROLE_STUDENT'),
(2, 'ROLE_INSTRUCTOR'),
(4, 'ROLE_ADMIN');

-- Users
-- Demo password for all users: password123
INSERT IGNORE INTO users
(username, password, enabled, authority_id)
VALUES
('student1','$2a$10$HmYX4v/S7rDWt1MnHjMer.S0hl8kLW3N8cmKK5S0Mde8ccwy50xP2',TRUE,1);

INSERT IGNORE INTO users
(username, password, enabled, authority_id)
VALUES
('instructor1','$2a$10$HmYX4v/S7rDWt1MnHjMer.S0hl8kLW3N8cmKK5S0Mde8ccwy50xP2',TRUE,2);

INSERT IGNORE INTO users
(username, password, enabled, authority_id)
VALUES
('admin1','$2a$10$HmYX4v/S7rDWt1MnHjMer.S0hl8kLW3N8cmKK5S0Mde8ccwy50xP2',TRUE,4);

-- Courses
INSERT IGNORE INTO courses
(course_code, course_name, category, description,
 duration_hours, capacity, active, created_at)
VALUES
('CS101', 'Introduction to Programming', 'Programming',
 'Basic programming concepts',
 20, 30, TRUE, NOW());

INSERT IGNORE INTO courses
(course_code, course_name, category, description,
 duration_hours, capacity, active, created_at)
VALUES
('DB201', 'Database Fundamentals', 'Database',
 'Introduction to relational databases',
 15, 25, TRUE, NOW());

INSERT IGNORE INTO courses
(course_code, course_name, category, description,
 duration_hours, capacity, active, created_at)
VALUES
('WEB301', 'Web Development', 'Web',
 'Introduction to web development',
 25, 20, TRUE, NOW());


-- Students
INSERT IGNORE INTO students
(student_number, full_name, email, active, created_at)
VALUES
('STU0011', 'Sarah Mubarak', 'sara@example.com', TRUE, NOW());

INSERT IGNORE INTO students
(student_number, full_name, email, active, created_at)
VALUES
('STU002', 'Nora Tariq', 'nora@example.com', TRUE, NOW());

INSERT IGNORE INTO students
(student_number, full_name, email, active, created_at)
VALUES
('STU003', 'Rahaf Abdulaziz', 'Rahaf@example.com', TRUE, NOW());