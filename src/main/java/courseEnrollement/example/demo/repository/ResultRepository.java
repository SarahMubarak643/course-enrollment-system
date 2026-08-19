package courseEnrollement.example.demo.repository;

import courseEnrollement.example.demo.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {

    Optional<Result> findByEnrollmentEnrollmentId(Long enrollmentId);

    boolean existsByEnrollmentEnrollmentId(Long enrollmentId);

    @Query("SELECT AVG(r.score) FROM Result r")
    Double findAverageScore();

    List<Result> findByEnrollmentStudentStudentId(Long studentId);

    @Query("SELECT AVG(r.score) FROM Result r WHERE r.enrollment.student.studentId = :studentId")
    Double findAverageScoreByStudentId(@Param("studentId") Long studentId);
}

