package courseEnrollement.example.demo.service;

import courseEnrollement.example.demo.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> getAllCourses();

    List<Course> getCoursesSortedByDuration(String direction);

    Page<Course> getCoursesPaginated(int page, int size);

    Page<Course> getCoursesPaginated(
            String keyword, String category, Boolean active, Pageable pageable);

    Optional<Course> getCourseById(Long id);

    List<Course> getCoursesByCategory(String category);

    Course createCourse(Course course);

    Course updateCourse(Long id, Course course);

    Course patchCourse(Long id, Course course);

    void deleteCourse(Long id);
}