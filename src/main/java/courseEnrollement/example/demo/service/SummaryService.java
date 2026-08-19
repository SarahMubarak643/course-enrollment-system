package courseEnrollement.example.demo.service;

import java.util.List;
import java.util.Map;

public interface SummaryService {

    Map<String, Object> getSummary();

    List<Map<String, Object>> getEnrollmentsByCourse();

    Map<String, Object> getMySummary(String username);
}
