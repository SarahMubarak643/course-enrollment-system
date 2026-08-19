package courseEnrollement.example.demo.repository;

import courseEnrollement.example.demo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>,
        JpaSpecificationExecutor<Course> {

    Optional<Course> findByCourseCode(String courseCode);

    List<Course> findByCategory(String category);

    boolean existsByCourseCode(String courseCode);

    @Query("SELECT c.courseCode as courseCode, c.courseName as courseName, "
            + "COUNT(e) as enrollmentCount "
            + "FROM Course c LEFT JOIN Enrollment e ON e.course = c "
            + "GROUP BY c.courseCode, c.courseName "
            + "ORDER BY c.courseName")
    List<CourseEnrollmentCount> countEnrollmentsByCourse();
}
