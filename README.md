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
| Student number, email, and course code are unique (409 if violated) | `StudentServiceImpl`, `CourseServiceImpl` |
| Duplicate enrollment (same student + course) is not allowed (409) | `EnrollmentServiceImpl.enrollStudent()` |
| Enrollment cannot exceed course capacity (409) | `EnrollmentServiceImpl.enrollStudent()` |
| Only valid status transitions are accepted; invalid ones return 409 | `EnrollmentServiceImpl.updateStatus()`, `EnrollmentStatus.java` |
| `reason` is required when transitioning to `REJECTED` or `WITHDRAWN` | `EnrollmentServiceImpl.updateStatus()` |
| A record can't be deleted while referenced by other data (409, not a raw 500) | `GlobalExceptionHandler.handleDataIntegrityViolation()` |
| Result score must be between 0 and 100; duplicate result for an enrollment is rejected (409) | `ResultServiceImpl` |
 
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
| `/api/auth/login` | — | Open (no login required to log in) |
| `/api/users/**` | ADMIN only | ADMIN only |
| `/api/courses/**` | STUDENT, INSTRUCTOR, ADMIN | ADMIN only |
| `/api/students/me` | STUDENT, INSTRUCTOR, ADMIN (own profile only) | — |
| `/api/students/**` (list, by id) | INSTRUCTOR, ADMIN only | INSTRUCTOR, ADMIN |
| `/api/enrollments/me` | STUDENT, INSTRUCTOR, ADMIN (own enrollments only) | — |
| `/api/enrollments/**` | STUDENT, INSTRUCTOR, ADMIN | INSTRUCTOR, ADMIN |
| `/api/results/me` | STUDENT, INSTRUCTOR, ADMIN (own results only) | — |
| `/api/results/**` | STUDENT, INSTRUCTOR, ADMIN | INSTRUCTOR, ADMIN |
| `/api/summary/me` | STUDENT, INSTRUCTOR, ADMIN (own summary only) | — |
| `/api/summary`, `/api/summary/courses` | INSTRUCTOR, ADMIN only | — |
 
CORS is enabled for the Angular development server (`http://localhost:4200`) — see `SecurityConfig.corsConfigurationSource()`.
401/403 responses are JSON (`{"error": "..."}`), not the browser's default Basic Auth prompt — see `JsonAuthEntryPoint` / `JsonAccessDeniedHandler`.
 
### Demo Users
 
| Username | Password | Role |
|---|---|---|
| student1 | ______ | STUDENT |
| instructor1 | ______ | INSTRUCTOR |
| admin1 | ______ | ADMIN |
 
Passwords are stored hashed with BCrypt; the values above are the plain-text passwords used for login/testing.
 
## 8. REST API Endpoints
 
### Authentication — `/api/auth`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/login` | Verifies username/password against the database and returns `{ username, role }`. Returns `401` on bad credentials. |
 
### Students — `/api/students`
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/students` | List all students (INSTRUCTOR/ADMIN only) |
| GET | `/api/students/me` | The logged-in STUDENT's own profile |
| GET | `/api/students/{id}` | Get student by id (INSTRUCTOR/ADMIN only) |
| POST | `/api/students` | Create student (validated, custom `StudentValidator`) |
| PUT | `/api/students/{id}` | Update student |
| DELETE | `/api/students/{id}` | Delete student |
 
### Courses — `/api/courses`
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/courses` | List all courses |
| GET | `/api/courses/page?page=&size=&sort=&keyword=&category=&active=` | Paginated, sortable (any column), keyword search (name/code), category + active filters |
| GET | `/api/courses/{id}` | Get course by id |
| GET | `/api/courses/category/{category}` | Custom finder by category |
| POST | `/api/courses` | Create course (duplicate course code → `409`) |
| PUT | `/api/courses/{id}` | Full update |
| PATCH | `/api/courses/{id}` | Partial update |
| DELETE | `/api/courses/{id}` | Delete course (referenced by an enrollment → `409`) |
 
### Enrollments — `/api/enrollments`
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/enrollments` | List all enrollments |
| GET | `/api/enrollments/me` | The logged-in STUDENT's own enrollments |
| GET | `/api/enrollments/{id}` | Get enrollment by id |
| GET | `/api/enrollments/student/{studentId}` | Custom finder by student |
| GET | `/api/enrollments/course/{courseId}` | Custom finder by course |
| POST | `/api/enrollments?studentId=&courseId=` | Enroll a student (checks duplicates and capacity, both → `409`); status starts at `ENROLLED` |
| PUT | `/api/enrollments/{id}/status?status=&reason=` | Change status. Only valid transitions are accepted (invalid → `409`); `reason` is required for `REJECTED`/`WITHDRAWN` |
| DELETE | `/api/enrollments/{id}` | Delete enrollment |
 
**Workflow:** `ENROLLED → APPROVED, REJECTED, WITHDRAWN` &nbsp;•&nbsp; `APPROVED → COMPLETED, WITHDRAWN` &nbsp;•&nbsp; `REJECTED`/`WITHDRAWN`/`COMPLETED` are terminal. See `EnrollmentStatus.java`. Every `Enrollment` JSON response also includes a computed `availableActions` array so the frontend never has to duplicate this table.
 
### Results — `/api/results`
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/results` | List all results |
| GET | `/api/results/me` | The logged-in STUDENT's own results |
| GET | `/api/results/{id}` | Get result by id |
| GET | `/api/results/enrollment/{enrollmentId}` | Custom finder by enrollment (404 if none recorded yet) |
| POST | `/api/results?enrollmentId=&score=&completionStatus=` | Create result (validates score 0–100; duplicate result for the same enrollment → `409`) |
| PUT | `/api/results/{id}?score=&completionStatus=` | Update result |
| DELETE | `/api/results/{id}` | Delete result |
 
### Summary — `/api/summary`
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/summary` | `studentCount`, `enrollmentCount`, `averageScore` (INSTRUCTOR/ADMIN) |
| GET | `/api/summary/courses` | Enrollment count per course (INSTRUCTOR/ADMIN) |
| GET | `/api/summary/me` | `myEnrollmentCount`, `myCompletedCount`, `myAverageScore` for the logged-in STUDENT |
 
Total: **33 REST endpoints** (plus 3 Thymeleaf MVC routes, see section 10), exceeding the 15-endpoint minimum.
 
## 9. Validation and Error Handling
 
- Bean Validation (`@Valid`) on `Student`, `Course`, and `Enrollment` create/update requests
- Custom `StudentValidator` (`@InitBinder`) — enforces that `studentNumber` starts with `STU`
- `GlobalExceptionHandler` (`@RestControllerAdvice`) handles:
  - `IllegalArgumentException` → `400 Bad Request`
  - `MethodArgumentNotValidException` → `400 Bad Request` with field errors
  - `ResourceNotFoundException` → `404 Not Found`
  - `AuthenticationException` → `401 Unauthorized`
  - `BusinessConflictException` (duplicates, capacity, invalid workflow transition) → `409 Conflict`
  - `DataIntegrityViolationException` (deleting a record still referenced elsewhere) → `409 Conflict`
- `JsonAuthEntryPoint` / `JsonAccessDeniedHandler` return JSON (not the browser's native Basic Auth prompt / an HTML error page) for 401/403
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