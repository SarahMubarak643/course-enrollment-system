package courseEnrollement.example.demo.controller;

import courseEnrollement.example.demo.service.SummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/summary")
public class SummaryRestController {

    private final SummaryService summaryService;

    public SummaryRestController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getSummary() {

        return ResponseEntity.ok(summaryService.getSummary());
    }

    @GetMapping("/courses")
    public ResponseEntity<List<Map<String, Object>>> getEnrollmentsByCourse() {

        return ResponseEntity.ok(summaryService.getEnrollmentsByCourse());
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMySummary(Authentication authentication) {

        return ResponseEntity.ok(summaryService.getMySummary(authentication.getName()));
    }
}