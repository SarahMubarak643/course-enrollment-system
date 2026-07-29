# Course Enrollment and Results System

COOP Backend Final Project — Individual Spring Boot Application.

## 1. Project Overview

| Item | Detail |
|---|---|
| Business Scenario | A learning department needs a backend system to manage students, courses, enrollments, and course results. |
| Implemented Entities | `Student`, `Course`, `Enrollment`, `Result`, `User`, `Authority` |
| Roles | `STUDENT`, `INSTRUCTOR`, `ADMIN` |
| Java Version | 21 |
| Framework | Spring Boot (Spring Boot Starter Parent 4.1.0) |

## 2. Technology Stack

- Java 21
- Spring Boot: Web MVC, Data JPA, Security, Validation, Thymeleaf, Actuator
- MySQL (`mysql-connector-j`)
- springdoc-openapi (Swagger UI)
- Maven

## 3. Architecture and Package Structure

```
courseEnrollement.example.demo
├── controller        # REST controllers + one MVC controller
├── entity             # JPA entities: User, Authority, Student, Course, Enrollment, Result
├── repository          # Spring Data JPA repositories
├── service              # Service interfaces
│   └── impl               # Service implementations
├── security             # SecurityConfig, CustomUserDetailsService
├── validator            # StudentValidator (custom Validator)
├── exception            # ResourceNotFoundException, GlobalExceptionHandler (@RestControllerAdvice)
└── DemoApplication.java
```

Each business module (Student, Course, Enrollment, Result, Summary) follows the same layered pattern:
`Controller → Service (interface) → ServiceImpl → Repository → Entity`.

Dependency injection is done through **constructor injection** across all controllers and services.

## 4. Database Design

- Entities: `authorities`, `users`, `students`, `courses`, `enrollments`, `results`
- Relationships:
  - `users` (many) → `authorities` (one) — each user has a single role
  - `students` (one) → `users` (one) — a student profile is linked to one user account
  - `enrollments` (many) → `students` (one), `enrollments` (many) → `courses` (one)
  - `results` (one) → `enrollments` (one) — one result per enrollment

Files:
- `src/main/resources/schema.sql` — table definitions, foreign keys, and the `chk_score` constraint (score between 0–100)
- `src/main/resources/data.sql` — sample data for authorities, users, students, and courses
- `src/main/resources/queries.sql` — CRUD examples, a JOIN query, two aggregate queries (`COUNT`, `AVG`), and a `VIEW` (`student_results_view`)

## 5. Business Rules (Service Layer)

| Rule | Where enforced |
|---|---|
| Student number, email, and course code are unique | `StudentServiceImpl`, `CourseServiceImpl` |
| Duplicate enrollment (same student + course) is not allowed | `EnrollmentServiceImpl.enrollStudent()` |
| Enrollment cannot exceed course capacity | `EnrollmentServiceImpl.enrollStudent()` |
| Result score must be between 0 and 100 | `ResultServiceImpl.validateScore()` |

## 6. Build and Run Locally

```bash
# Build
./mvnw clean install

# Run
./mvnw spring-boot:run
```

Application runs on **port 8081** (see `application.properties`).

### application.properties highlights

| Property | Value |
|---|---|
| `server.port` | `8081` |
| `training.system.name` (custom `@Value` property) | `Course Enrollment and Results System` |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/course_enrollment_db` |
| `spring.datasource.username` / `password` | via `DB_USERNAME` / `DB_PASSWORD` environment variables |
| `spring.jpa.hibernate.ddl-auto` | `update` |

### Actuator

- Health: `http://localhost:8081/actuator/health`
- Info: `http://localhost:8081/actuator/info`

## 7. Authentication and Roles

- Authentication: HTTP Basic, backed by `CustomUserDetailsService` reading from the `users`/`authorities` tables
- Passwords are stored using `BCryptPasswordEncoder`
- Roles: `STUDENT`, `INSTRUCTOR`, `ADMIN`

| Area | GET | Write (POST/PUT/PATCH/DELETE) |
|---|---|---|
| `/api/users/**` | ADMIN only | ADMIN only |
| `/api/courses/**` | STUDENT, INSTRUCTOR, ADMIN | ADMIN only |
| `/api/students/**` | STUDENT, INSTRUCTOR, ADMIN | ADMIN only |
| `/api/enrollments/**` | STUDENT, INSTRUCTOR, ADMIN | INSTRUCTOR, ADMIN |
| `/api/results/**` | STUDENT, INSTRUCTOR, ADMIN | INSTRUCTOR, ADMIN |
| `/api/summary` | INSTRUCTOR, ADMIN | — |

### Demo Users

| Username | Password | Role |
|---|---|---|
| student1 | ______ | STUDENT |
| instructor1 | ______ | INSTRUCTOR |
| admin1 | ______ | ADMIN |

Passwords are stored hashed with BCrypt; the values above are the plain-text passwords used for login/testing.

## 8. REST API Endpoints

### Students — `/api/students`
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/students` | List all students |
| GET | `/api/students/{id}` | Get student by id |
| POST | `/api/students` | Create student (validated, custom `StudentValidator`) |
| PUT | `/api/students/{id}` | Update student |
| DELETE | `/api/students/{id}` | Delete student |

### Courses — `/api/courses`
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/courses?sort=` | List all courses, optional sort by duration |
| GET | `/api/courses/page?page=&size=` | Paginated courses |
| GET | `/api/courses/{id}` | Get course by id |
| GET | `/api/courses/category/{category}` | Custom finder by category |
| POST | `/api/courses` | Create course |
| PUT | `/api/courses/{id}` | Full update |
| PATCH | `/api/courses/{id}` | Partial update |
| DELETE | `/api/courses/{id}` | Delete course |

### Enrollments — `/api/enrollments`
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/enrollments` | List all enrollments |
| GET | `/api/enrollments/{id}` | Get enrollment by id |
| GET | `/api/enrollments/student/{studentId}` | Custom finder by student |
| GET | `/api/enrollments/course/{courseId}` | Custom finder by course |
| POST | `/api/enrollments?studentId=&courseId=` | Enroll a student (checks duplicates and capacity) |
| PUT | `/api/enrollments/{id}/status?status=` | Update enrollment status |
| DELETE | `/api/enrollments/{id}` | Delete enrollment |

### Results — `/api/results`
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/results` | List all results |
| GET | `/api/results/{id}` | Get result by id |
| GET | `/api/results/enrollment/{enrollmentId}` | Custom finder by enrollment |
| POST | `/api/results?enrollmentId=&score=&completionStatus=` | Create result (validates score 0–100) |
| PUT | `/api/results/{id}?score=&completionStatus=` | Update result |
| DELETE | `/api/results/{id}` | Delete result |

### Summary — `/api/summary`
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/summary` | Returns `studentCount`, `enrollmentCount`, `averageScore` (COUNT / AVG) |

Total: **27 REST endpoints**, exceeding the 15-endpoint minimum.

## 9. Validation and Error Handling

- Bean Validation (`@Valid`) on `Student` and `Course` create/update requests
- Custom `StudentValidator` (`@InitBinder`) — enforces that `studentNumber` starts with `STU`
- `GlobalExceptionHandler` (`@RestControllerAdvice`) handles:
  - `IllegalArgumentException` → `400 Bad Request`
  - `ResourceNotFoundException` → `404 Not Found`
  - `MethodArgumentNotValidException` → `400 Bad Request` with field errors

## 10. Thymeleaf Pages

- List page: `GET /courses` — displays all courses, reads the custom `training.system.name` property
- Add form: `GET /courses/new`, submitted via `POST /courses`
- Stylesheet: `src/main/resources/static/style.css`

## 11. Swagger / API Docs

- Swagger UI: `http://localhost:8081/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`

## 12. Podman / Containers

### Build the JAR
```bash
./mvnw clean package
```

### Build the application image
```bash
podman build -t course-enrollment-app .
```

### Create a shared network
```bash
podman network create course-net
```

### Run MySQL container
```bash
podman run -d --name mysql-db --network course-net \
  -e MYSQL_ROOT_PASSWORD=______ \
  -e MYSQL_DATABASE=course_enrollment_db \
  -p 3306:3306 mysql:8
```

### Run the Spring Boot container
```bash
podman run -d --name course-enrollment-app --network course-net \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=______ \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql-db:3306/course_enrollment_db \
  -p 8081:8081 course-enrollment-app
```

### Verify
```bash
podman images
podman ps
podman logs course-enrollment-app
```

## 13. Git

- `.gitignore` excludes `target/`, IDE files, and local/sensitive configuration
- Development should continue on a feature branch and be merged into `main` with meaningful commits before submission

## 14. Postman

The Postman collection (`course-enrollment.postman_collection.json`) covers:
- GET, POST, PUT, PATCH, DELETE requests for each module
- One 404 request (invalid id)
- One 403 request (insufficient role)

Import the collection file into Postman to run the requests against `http://localhost:8081`.