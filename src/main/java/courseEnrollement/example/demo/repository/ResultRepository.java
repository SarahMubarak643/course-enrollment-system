package courseEnrollement.example.demo.repository;

import courseEnrollement.example.demo.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {

    Optional<Result> findByEnrollmentEnrollmentId(Long enrollmentId);

    boolean existsByEnrollmentEnrollmentId(Long enrollmentId);

    @Query("SELECT AVG(r.score) FROM Result r")
    Double findAverageScore();
}
