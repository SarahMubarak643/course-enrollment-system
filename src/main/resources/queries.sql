
-- Course Enrollment and Results System
-- SQL Query Examples

-- 1. CREATE (INSERT)
-- Add a new course

INSERT INTO courses
(course_code, course_name, category, description,
 duration_hours, capacity, active, created_at)
VALUES
('JAVA101', 'Java Programming', 'Programming',
 'Introduction to Java programming',
 30, 25, TRUE, NOW());

-- 2. READ (SELECT)
-- Display all courses

SELECT *
FROM courses;


-- 3. UPDATE
-- Update course capacity

UPDATE courses
SET capacity = 35
WHERE course_code = 'JAVA101';


-- 4. DELETE
-- Delete the sample course

DELETE FROM courses
WHERE course_code = 'JAVA101';


-- 5. JOIN
-- Display students and their enrolled courses


SELECT
    s.student_number,
    s.full_name,
    c.course_code,
    c.course_name,
    e.status
FROM enrollments e
JOIN students s
    ON e.student_id = s.student_id
JOIN courses c
    ON e.course_id = c.course_id;


-- 6. AGGREGATE
-- Count enrollments for each course

SELECT
    c.course_name,
    COUNT(e.enrollment_id) AS enrollment_count
FROM courses c
LEFT JOIN enrollments e
    ON c.course_id = e.course_id
GROUP BY c.course_id, c.course_name;


-- 7. AGGREGATE
-- Calculate average score

SELECT AVG(score) AS average_score
FROM results;


-- 8. VIEW
-- Create a view showing student results

CREATE OR REPLACE VIEW student_results_view AS
SELECT
    s.student_number,
    s.full_name,
    c.course_code,
    c.course_name,
    r.score,
    r.completion_status
FROM results r
JOIN enrollments e
    ON r.enrollment_id = e.enrollment_id
JOIN students s
    ON e.student_id = s.student_id
JOIN courses c
    ON e.course_id = c.course_id;


-- 9. Read data from the VIEW

SELECT *
FROM student_results_view;