package courseEnrollement.example.demo.validator;

import courseEnrollement.example.demo.entity.Student;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class StudentValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Student.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Student student = (Student) target;

        if (student.getStudentNumber() != null
                && !student.getStudentNumber().startsWith("STU")) {

            errors.rejectValue(
                    "studentNumber",
                    "studentNumber.invalid",
                    "Student number must start with STU"
            );
        }
    }
}