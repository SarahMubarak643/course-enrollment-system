package courseEnrollement.example.demo.service.impl;

import courseEnrollement.example.demo.entity.Course;
import courseEnrollement.example.demo.repository.CourseRepository;
import courseEnrollement.example.demo.service.CourseService;
import courseEnrollement.example.demo.exception.ResourceNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public List<Course> getCoursesSortedByDuration(String direction) {

        Sort sort;

        if ("desc".equalsIgnoreCase(direction)) {
            sort = Sort.by("durationHours").descending();
        } else {
            sort = Sort.by("durationHours").ascending();
        }

        return courseRepository.findAll(sort);
    }

    @Override
    public Page<Course> getCoursesPaginated(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return courseRepository.findAll(pageable);
    }

    @Override
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category);
    }

    @Override
    public Course createCourse(Course course) {

        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new IllegalArgumentException("Course code already exists");
        }

        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Long id, Course course) {

        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: "+id));

        if (!existingCourse.getCourseCode().equals(course.getCourseCode())
                && courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new IllegalArgumentException("Course code already exists");
        }

        existingCourse.setCourseCode(course.getCourseCode());
        existingCourse.setCourseName(course.getCourseName());
        existingCourse.setCategory(course.getCategory());
        existingCourse.setDescription(course.getDescription());
        existingCourse.setDurationHours(course.getDurationHours());
        existingCourse.setCapacity(course.getCapacity());
        existingCourse.setActive(course.getActive());

        return courseRepository.save(existingCourse);
    }

    @Override
    public Course patchCourse(Long id, Course course) {

        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: "+id));

        if (course.getCourseCode() != null) {
            if (!existingCourse.getCourseCode().equals(course.getCourseCode())
                    && courseRepository.existsByCourseCode(course.getCourseCode())) {
                throw new IllegalArgumentException("Course code already exists");
            }

            existingCourse.setCourseCode(course.getCourseCode());
        }

        if (course.getCourseName() != null) {
            existingCourse.setCourseName(course.getCourseName());
        }

        if (course.getCategory() != null) {
            existingCourse.setCategory(course.getCategory());
        }

        if (course.getDescription() != null) {
            existingCourse.setDescription(course.getDescription());
        }

        if (course.getDurationHours() != null) {
            existingCourse.setDurationHours(course.getDurationHours());
        }

        if (course.getCapacity() != null) {
            existingCourse.setCapacity(course.getCapacity());
        }

        if (course.getActive() != null) {
            existingCourse.setActive(course.getActive());
        }

        return courseRepository.save(existingCourse);
    }

    @Override
    public void deleteCourse(Long id) {

        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with id: "+id);
        }

        courseRepository.deleteById(id);
    }
}
