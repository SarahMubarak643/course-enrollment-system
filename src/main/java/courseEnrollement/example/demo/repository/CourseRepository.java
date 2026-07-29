package courseEnrollement.example.demo.repository;

import courseEnrollement.example.demo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseCode(String courseCode);

    List<Course> findByCategory(String category);

    boolean existsByCourseCode(String courseCode);
}
