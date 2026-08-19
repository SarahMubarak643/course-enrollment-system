package courseEnrollement.example.demo.controller;

import courseEnrollement.example.demo.entity.Enrollment;
import courseEnrollement.example.demo.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentRestController {

    private final EnrollmentService enrollmentService;

    public EnrollmentRestController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("/me")
    public List<Enrollment> getMyEnrollments(Authentication authentication) {
        return enrollmentService.getMyEnrollments(authentication.getName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(
            @PathVariable Long id) {

        return enrollmentService.getEnrollmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    public List<Enrollment> getEnrollmentsByStudent(
            @PathVariable Long studentId) {

        return enrollmentService.getEnrollmentsByStudent(studentId);
    }

    @GetMapping("/course/{courseId}")
    public List<Enrollment> getEnrollmentsByCourse(
            @PathVariable Long courseId) {

        return enrollmentService.getEnrollmentsByCourse(courseId);
    }

    @PostMapping
    public ResponseEntity<Enrollment> enrollStudent(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {

        Enrollment enrollment =
                enrollmentService.enrollStudent(studentId, courseId);

        return ResponseEntity.ok(enrollment);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Enrollment> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String reason) {

        Enrollment enrollment =
                enrollmentService.updateStatus(id, status, reason);

        return ResponseEntity.ok(enrollment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(
            @PathVariable Long id) {

        enrollmentService.deleteEnrollment(id);

        return ResponseEntity.noContent().build();
    }
}