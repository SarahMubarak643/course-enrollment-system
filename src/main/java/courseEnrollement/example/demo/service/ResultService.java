package courseEnrollement.example.demo.service;

import courseEnrollement.example.demo.entity.Result;

import java.util.List;
import java.util.Optional;

public interface ResultService {

    List<Result> getAllResults();

    Optional<Result> getResultById(Long id);

    Optional<Result> getResultByEnrollment(Long enrollmentId);

    List<Result> getMyResults(String username);

    Result createResult(
            Long enrollmentId,
            Double score,
            String completionStatus
    );

    Result updateResult(
            Long resultId,
            Double score,
            String completionStatus
    );

    void deleteResult(Long id);
}