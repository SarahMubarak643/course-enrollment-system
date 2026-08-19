package courseEnrollement.example.demo.controller;

import courseEnrollement.example.demo.entity.Result;
import courseEnrollement.example.demo.service.ResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class ResultRestController {

    private final ResultService resultService;

    public ResultRestController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping
    public List<Result> getAllResults() {
        return resultService.getAllResults();
    }

    @GetMapping("/me")
    public List<Result> getMyResults(Authentication authentication) {
        return resultService.getMyResults(authentication.getName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getResultById(
            @PathVariable Long id) {

        return resultService.getResultById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<Result> getResultByEnrollment(
            @PathVariable Long enrollmentId) {

        return resultService.getResultByEnrollment(enrollmentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Result> createResult(
            @RequestParam Long enrollmentId,
            @RequestParam Double score,
            @RequestParam String completionStatus) {

        Result result = resultService.createResult(
                enrollmentId,
                score,
                completionStatus
        );

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> updateResult(
            @PathVariable Long id,
            @RequestParam Double score,
            @RequestParam String completionStatus) {

        Result result = resultService.updateResult(
                id,
                score,
                completionStatus
        );

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(
            @PathVariable Long id) {

        resultService.deleteResult(id);

        return ResponseEntity.noContent().build();
    }
}
