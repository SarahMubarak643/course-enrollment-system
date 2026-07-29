package courseEnrollement.example.demo.service.impl;

import courseEnrollement.example.demo.entity.Student;
import courseEnrollement.example.demo.exception.ResourceNotFoundException;
import courseEnrollement.example.demo.repository.StudentRepository;
import courseEnrollement.example.demo.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public Student createStudent(Student student) {

        if (studentRepository.existsByStudentNumber(student.getStudentNumber())) {
            throw new IllegalArgumentException("Student number already exists");
        }

        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Long id, Student student) {

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        if (!existingStudent.getStudentNumber().equals(student.getStudentNumber())
                && studentRepository.existsByStudentNumber(student.getStudentNumber())) {
            throw new IllegalArgumentException("Student number already exists");
        }

        if (!existingStudent.getEmail().equals(student.getEmail())
                && studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        existingStudent.setStudentNumber(student.getStudentNumber());
        existingStudent.setFullName(student.getFullName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setActive(student.getActive());

        return studentRepository.save(existingStudent);
    }

    @Override
    public void deleteStudent(Long id) {

        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: "+ id);
        }

        studentRepository.deleteById(id);
    }
}