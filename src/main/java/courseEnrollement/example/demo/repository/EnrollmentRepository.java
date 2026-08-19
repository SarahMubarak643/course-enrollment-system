package courseEnrollement.example.demo.repository;

import courseEnrollement.example.demo.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentStudentId(Long studentId);

    List<Enrollment> findByCourseCourseId(Long courseId);

    boolean existsByStudentStudentIdAndCourseCourseId(
            Long studentId,
            Long courseId
    );

    long countByCourseCourseId(Long courseId);

    long countByStudentStudentId(Long studentId);

    long countByStudentStudentIdAndStatus(Long studentId, String status);
}