package courseEnrollement.example.demo.service.impl;

import courseEnrollement.example.demo.entity.Enrollment;
import courseEnrollement.example.demo.entity.Result;
import courseEnrollement.example.demo.repository.EnrollmentRepository;
import courseEnrollement.example.demo.repository.ResultRepository;
import courseEnrollement.example.demo.service.ResultService;
import courseEnrollement.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;
    private final EnrollmentRepository enrollmentRepository;

    public ResultServiceImpl(
            ResultRepository resultRepository,
            EnrollmentRepository enrollmentRepository) {

        this.resultRepository = resultRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }

    @Override
    public Optional<Result> getResultById(Long id) {
        return resultRepository.findById(id);
    }

    @Override
    public Optional<Result> getResultByEnrollment(Long enrollmentId) {
        return resultRepository.findByEnrollmentEnrollmentId(enrollmentId);
    }

    @Override
    public Result createResult(
            Long enrollmentId,
            Double score,
            String completionStatus) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId));

        if (resultRepository.existsByEnrollmentEnrollmentId(enrollmentId)) {
            throw new IllegalArgumentException(
                    "Result already exists for this enrollment"
            );
        }

        validateScore(score);

        Result result = new Result();
        result.setEnrollment(enrollment);
        result.setScore(score);
        result.setCompletionStatus(completionStatus);

        return resultRepository.save(result);
    }

    @Override
    public Result updateResult(
            Long resultId,
            Double score,
            String completionStatus) {

        Result result = resultRepository.findById(resultId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Result not found with id:" + resultId));

        validateScore(score);

        result.setScore(score);
        result.setCompletionStatus(completionStatus);

        return resultRepository.save(result);
    }

    @Override
    public void deleteResult(Long id) {

        if (!resultRepository.existsById(id)) {
            throw new ResourceNotFoundException("Result not found with id: "+id);
        }

        resultRepository.deleteById(id);
    }

    private void validateScore(Double score) {

        if (score == null || score < 0 || score > 100) {
            throw new IllegalArgumentException(
                    "Score must be between 0 and 100"
            );
        }
    }
}