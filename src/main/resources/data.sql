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
('STU001', 'Sarah Mubarak', 'sara@example.com', TRUE, NOW());

INSERT IGNORE INTO students
(student_number, full_name, email, active, created_at)
VALUES
('STU002', 'Nora Tariq', 'nora@example.com', TRUE, NOW());

INSERT IGNORE INTO students
(student_number, full_name, email, active, created_at)
VALUES
('STU003', 'Rahaf Abdulaziz', 'Rahaf@example.com', TRUE, NOW());