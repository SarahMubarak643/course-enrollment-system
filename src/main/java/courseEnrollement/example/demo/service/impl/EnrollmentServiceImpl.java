package courseEnrollement.example.demo.service.impl;

import courseEnrollement.example.demo.entity.Course;
import courseEnrollement.example.demo.entity.Enrollment;
import courseEnrollement.example.demo.entity.EnrollmentStatus;
import courseEnrollement.example.demo.entity.Student;
import courseEnrollement.example.demo.exception.BusinessConflictException;
import courseEnrollement.example.demo.exception.ResourceNotFoundException;
import courseEnrollement.example.demo.repository.CourseRepository;
import courseEnrollement.example.demo.repository.EnrollmentRepository;
import courseEnrollement.example.demo.repository.StudentRepository;
import courseEnrollement.example.demo.service.EnrollmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentServiceImpl(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {

        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public Optional<Enrollment> getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id);
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudentStudentId(studentId);
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourseCourseId(courseId);
    }

    @Override
    public List<Enrollment> getMyEnrollments(String username) {

        Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No student profile is linked to this account"));

        return enrollmentRepository.findByStudentStudentId(student.getStudentId());
    }

    @Override
    public Enrollment enrollStudent(Long studentId, Long courseId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found with id: " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found with id: "+ courseId));

        if (enrollmentRepository
                .existsByStudentStudentIdAndCourseCourseId(studentId, courseId)) {

            throw new BusinessConflictException(
                    "Student is already enrolled in this course"
            );
        }

        long currentEnrollments =
                enrollmentRepository.countByCourseCourseId(courseId);

        if (currentEnrollments >= course.getCapacity()) {
            throw new BusinessConflictException("Course is full");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus("ENROLLED");

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment updateStatus(Long id, String status, String reason) {

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Enrollment not found with id: "+ id));

        EnrollmentStatus currentStatus;

        try {
            currentStatus = EnrollmentStatus.valueOf(enrollment.getStatus());
        } catch (IllegalArgumentException ex) {
            // Existing rows created before this enum existed might not
            // match a known value; treat that as ENROLLED-like and let
            // the requested value validation below still apply.
            currentStatus = EnrollmentStatus.ENROLLED;
        }

        EnrollmentStatus requestedStatus;

        try {
            requestedStatus = EnrollmentStatus.valueOf(status);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Unknown enrollment status: " + status
                            + ". Valid values are: ENROLLED, APPROVED, REJECTED, WITHDRAWN, COMPLETED"
            );
        }

        if (!currentStatus.canTransitionTo(requestedStatus.name())) {
            throw new BusinessConflictException(
                    "Cannot change enrollment status from "
                            + currentStatus + " to " + requestedStatus
            );
        }

        if (EnrollmentStatus.requiresReason(requestedStatus.name())
                && (reason == null || reason.isBlank())) {
            throw new IllegalArgumentException(
                    "A reason is required when setting status to " + requestedStatus
            );
        }

        enrollment.setStatus(requestedStatus.name());
        enrollment.setReason(reason);

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public void deleteEnrollment(Long id) {

        if (!enrollmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Enrollment not found with id: "+ id);
        }

        enrollmentRepository.deleteById(id);
    }
}