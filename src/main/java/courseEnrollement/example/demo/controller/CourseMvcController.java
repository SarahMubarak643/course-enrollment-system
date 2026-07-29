package courseEnrollement.example.demo.controller;

import courseEnrollement.example.demo.entity.Course;
import courseEnrollement.example.demo.service.CourseService;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Value;

@Controller
public class CourseMvcController {

    private final CourseService courseService;

    @Value("${training.system.name}")
    private String systemName;

    public CourseMvcController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public String showCourses(Model model) {

        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("systemName", systemName);

        return "courses";
    }

    @GetMapping("/courses/new")
    public String showAddCourseForm(Model model) {

        model.addAttribute("course", new Course());

        return "add-course";
    }

    @PostMapping("/courses")
    public String addCourse(
            @Valid @ModelAttribute("course") Course course,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "add-course";
        }

        courseService.createCourse(course);

        return "redirect:/courses";
    }

}