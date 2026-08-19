package courseEnrollement.example.demo.repository;

import courseEnrollement.example.demo.entity.Course;
import org.springframework.data.jpa.domain.Specification;

public final class CourseSpecifications {

    private CourseSpecifications() {
    }

    public static Specification<Course> withFilters(
            String keyword, String category, Boolean active) {

        return (root, query, builder) -> {

            var predicate = builder.conjunction();

            if (keyword != null && !keyword.isBlank()) {
                String likePattern = "%" + keyword.toLowerCase() + "%";

                predicate = builder.and(predicate, builder.or(
                        builder.like(builder.lower(root.get("courseName")), likePattern),
                        builder.like(builder.lower(root.get("courseCode")), likePattern)
                ));
            }

            if (category != null && !category.isBlank()) {
                predicate = builder.and(predicate,
                        builder.equal(root.get("category"), category));
            }

            if (active != null) {
                predicate = builder.and(predicate,
                        builder.equal(root.get("active"), active));
            }

            return predicate;
        };
    }
}
