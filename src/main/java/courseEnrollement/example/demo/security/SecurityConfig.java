package courseEnrollement.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JsonAuthEntryPoint jsonAuthEntryPoint;
    private final JsonAccessDeniedHandler jsonAccessDeniedHandler;

    public SecurityConfig(
            JsonAuthEntryPoint jsonAuthEntryPoint,
            JsonAccessDeniedHandler jsonAccessDeniedHandler) {

        this.jsonAuthEntryPoint = jsonAuthEntryPoint;
        this.jsonAccessDeniedHandler = jsonAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Lets the AuthController authenticate username/password manually
    // (used by the /api/auth/login endpoint) using the same
    // CustomUserDetailsService + BCrypt setup as HTTP Basic.
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {

        return authConfig.getAuthenticationManager();
    }

    // Allows the Angular development server (http://localhost:4200) to
    // call this API. Only that origin is allowed, not every origin.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> {})

                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(jsonAuthEntryPoint)
                        .accessDeniedHandler(jsonAccessDeniedHandler)
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
            "/swagger-ui/**","/swagger-ui.html","/v3/api-docs/**")
                        .permitAll()

                        // Login endpoint must be reachable without being logged in yet
                        .requestMatchers("/api/auth/**")
                        .permitAll()

                        // Browser CORS preflight requests carry no credentials,
                        // so they must be allowed through before the role rules below
                        .requestMatchers(HttpMethod.OPTIONS, "/**")
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
                        // Personal profile: any logged-in role may see their own record.
                        .requestMatchers(HttpMethod.GET, "/api/students/me")
                        .hasAnyRole("STUDENT", "INSTRUCTOR", "ADMIN")

                        // Full student list / any student by id: INSTRUCTOR/ADMIN only.
                        // A plain STUDENT must use /api/students/me instead.
                        .requestMatchers(HttpMethod.GET, "/api/students/**")
                        .hasAnyRole("INSTRUCTOR", "ADMIN")

                        .requestMatchers("/api/students/**")
                        .hasAnyRole("INSTRUCTOR", "ADMIN")

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

                        // SUMMARY / DASHBOARD
                        // Personal dashboard: any logged-in role may see their own summary.
                        .requestMatchers(HttpMethod.GET, "/api/summary/me")
                        .hasAnyRole("STUDENT", "INSTRUCTOR", "ADMIN")

                        // Full dashboard reports: INSTRUCTOR/ADMIN only.
                        .requestMatchers(HttpMethod.GET, "/api/summary/**")
                        .hasAnyRole("INSTRUCTOR", "ADMIN")

                        // Anything else requires login
                        .anyRequest().authenticated()
                )

                .httpBasic(basic -> {});

        return http.build();
    }
}