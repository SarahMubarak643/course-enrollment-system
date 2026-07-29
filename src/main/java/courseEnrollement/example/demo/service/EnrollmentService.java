package courseEnrollement.example.demo.service;

import courseEnrollement.example.demo.entity.Enrollment;

import java.util.List;
import java.util.Optional;

public interface EnrollmentService {

    List<Enrollment> getAllEnrollments();

    Optional<Enrollment> getEnrollmentById(Long id);

    List<Enrollment> getEnrollmentsByStudent(Long studentId);

    List<Enrollment> getEnrollmentsByCourse(Long courseId);

    Enrollment enrollStudent(Long studentId, Long courseId);

    Enrollment updateStatus(Long id, String status);

    void deleteEnrollment(Long id);
}