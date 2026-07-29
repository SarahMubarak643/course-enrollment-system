package courseEnrollement.example.demo.controller;

import courseEnrollement.example.demo.entity.Course;
import courseEnrollement.example.demo.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseRestController {

    private final CourseService courseService;

    public CourseRestController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getAllCourses(
            @RequestParam(required = false) String sort) {

        if (sort != null) {
            return courseService.getCoursesSortedByDuration(sort);
        }

        return courseService.getAllCourses();
    }

    @GetMapping("/page")
    public Page<Course> getCoursesPaginated(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {

        return courseService.getCoursesPaginated(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public List<Course> getCoursesByCategory(
            @PathVariable String category) {

        return courseService.getCoursesByCategory(category);
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(
            @Valid @RequestBody Course course) {

        Course savedCourse = courseService.createCourse(course);

        return ResponseEntity.ok(savedCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody Course course) {

        Course updatedCourse =
                courseService.updateCourse(id, course);

        return ResponseEntity.ok(updatedCourse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Course> patchCourse(
            @PathVariable Long id,
            @RequestBody Course course) {

        Course patchedCourse =
                courseService.patchCourse(id, course);

        return ResponseEntity.ok(patchedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long id) {

        courseService.deleteCourse(id);

        return ResponseEntity.noContent().build();
    }
}