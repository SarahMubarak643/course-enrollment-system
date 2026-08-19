package courseEnrollement.example.demo.service.impl;

import courseEnrollement.example.demo.entity.Student;
import courseEnrollement.example.demo.exception.ResourceNotFoundException;
import courseEnrollement.example.demo.repository.CourseEnrollmentCount;
import courseEnrollement.example.demo.repository.CourseRepository;
import courseEnrollement.example.demo.repository.EnrollmentRepository;
import courseEnrollement.example.demo.repository.ResultRepository;
import courseEnrollement.example.demo.repository.StudentRepository;
import courseEnrollement.example.demo.service.SummaryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SummaryServiceImpl implements SummaryService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ResultRepository resultRepository;
    private final CourseRepository courseRepository;

    public SummaryServiceImpl(
            StudentRepository studentRepository,
            EnrollmentRepository enrollmentRepository,
            ResultRepository resultRepository,
            CourseRepository courseRepository) {

        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.resultRepository = resultRepository;
        this.courseRepository = courseRepository;
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

    @Override
    public List<Map<String, Object>> getEnrollmentsByCourse() {

        List<CourseEnrollmentCount> rows = courseRepository.countEnrollmentsByCourse();
        List<Map<String, Object>> report = new ArrayList<>();

        for (CourseEnrollmentCount row : rows) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("courseCode", row.getCourseCode());
            entry.put("courseName", row.getCourseName());
            entry.put("enrollmentCount", row.getEnrollmentCount());
            report.add(entry);
        }

        return report;
    }

    @Override
    public Map<String, Object> getMySummary(String username) {

        Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No student profile is linked to this account"));

        long myEnrollmentCount =
                enrollmentRepository.countByStudentStudentId(student.getStudentId());

        long myCompletedCount = enrollmentRepository
                .countByStudentStudentIdAndStatus(student.getStudentId(), "COMPLETED");

        Double myAverageScore =
                resultRepository.findAverageScoreByStudentId(student.getStudentId());

        if (myAverageScore == null) {
            myAverageScore = 0.0;
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("myEnrollmentCount", myEnrollmentCount);
        summary.put("myCompletedCount", myCompletedCount);
        summary.put("myAverageScore", myAverageScore);

        return summary;
    }
}