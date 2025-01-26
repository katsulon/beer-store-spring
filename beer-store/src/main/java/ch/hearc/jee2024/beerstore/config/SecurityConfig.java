package ch.hearc.jee2024.beerstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF
                .authorizeHttpRequests(auth -> auth
                        // Public routes
                        .requestMatchers("/h2/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/manufacturer/**", "/beer/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user").hasRole("ADMIN") // Only admin can list users
                        .requestMatchers("/user/**").permitAll()
                        // Authenticated routes
                        .requestMatchers("/order/**").hasAnyRole("USER", "ADMIN")
                        // Admin routes
                        .requestMatchers("/beer/**", "manufacturer/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .userDetailsService(customUserDetailsService)
                .httpBasic(Customizer.withDefaults()) // Enable Basic Authentication
                .headers(headers -> headers.frameOptions().disable()) // Disable X-Frame-Options
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Define password hashing
    }
}


