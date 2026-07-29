package courseEnrollement.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
            "/swagger-ui/**","/swagger-ui.html","/v3/api-docs/**")
                        .permitAll()

                        // ADMIN: full access to everything
                        .requestMatchers("/api/users/**")
                        .hasRole("ADMIN")

                        // COURSES
                        .requestMatchers(HttpMethod.GET, "/api/courses/**")
                        .hasAnyRole("STUDENT", "INSTRUCTOR", "ADMIN")

                        .requestMatchers("/api/courses/**")
                        .hasRole("ADMIN")

                        // STUDENTS
                        .requestMatchers(HttpMethod.GET, "/api/students/**")
                        .hasAnyRole("STUDENT", "INSTRUCTOR", "ADMIN")

                        .requestMatchers("/api/students/**")
                        .hasRole("ADMIN")

                        // ENROLLMENTS
                        .requestMatchers(HttpMethod.GET, "/api/enrollments/**")
                        .hasAnyRole("STUDENT", "INSTRUCTOR", "ADMIN")

                        .requestMatchers("/api/enrollments/**")
                        .hasAnyRole("INSTRUCTOR", "ADMIN")

                        // RESULTS
                        .requestMatchers(HttpMethod.GET, "/api/results/**")
                        .hasAnyRole("STUDENT", "INSTRUCTOR", "ADMIN")

                        .requestMatchers("/api/results/**")
                        .hasAnyRole("INSTRUCTOR", "ADMIN")

                        // SUMMARY
                        .requestMatchers(HttpMethod.GET, "/api/summary")
                        .hasAnyRole("INSTRUCTOR", "ADMIN")

                        // Anything else requires login
                        .anyRequest().authenticated()
                )

                .httpBasic(basic -> {});

        return http.build();
    }
}