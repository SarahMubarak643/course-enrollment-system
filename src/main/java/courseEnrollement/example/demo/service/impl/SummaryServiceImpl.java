package courseEnrollement.example.demo.service.impl;

import courseEnrollement.example.demo.repository.EnrollmentRepository;
import courseEnrollement.example.demo.repository.ResultRepository;
import courseEnrollement.example.demo.repository.StudentRepository;
import courseEnrollement.example.demo.service.SummaryService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SummaryServiceImpl implements SummaryService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ResultRepository resultRepository;

    public SummaryServiceImpl(
            StudentRepository studentRepository,
            EnrollmentRepository enrollmentRepository,
            ResultRepository resultRepository) {

        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.resultRepository = resultRepository;
    }

    @Override
    public Map<String, Object> getSummary() {

        long studentCount = studentRepository.count();
        long enrollmentCount = enrollmentRepository.count();
        Double averageScore = resultRepository.findAverageScore();

        if (averageScore == null) {
            averageScore = 0.0;
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("studentCount", studentCount);
        summary.put("enrollmentCount", enrollmentCount);
        summary.put("averageScore", averageScore);

        return summary;
    }
}