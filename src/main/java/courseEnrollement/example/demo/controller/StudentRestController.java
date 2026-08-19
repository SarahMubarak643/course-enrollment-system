package courseEnrollement.example.demo.controller;

import courseEnrollement.example.demo.entity.Student;
import courseEnrollement.example.demo.service.StudentService;
import courseEnrollement.example.demo.validator.StudentValidator;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {

    private final StudentService studentService;
    private final StudentValidator studentValidator;

    public StudentRestController(StudentService studentService,StudentValidator studentValidator) {

        this.studentService = studentService;
        this.studentValidator = studentValidator;
    }
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(studentValidator);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/me")
    public Student getMyProfile(Authentication authentication) {
        return studentService.getMyProfile(authentication.getName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {

        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(
            @Valid @RequestBody Student student) {

        Student savedStudent = studentService.createStudent(student);

        return ResponseEntity.ok(savedStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody Student student) {

        Student updatedStudent =
                studentService.updateStudent(id, student);

        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {

        studentService.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }
}