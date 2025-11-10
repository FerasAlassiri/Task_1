package com.example.task_1.config;

import com.example.task_1.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
@EnableMethodSecurity // Enables method-level security annotations like @PreAuthorize
public class SecurityConfig {

    @Bean // Password encoder for hashing user passwords
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // Authentication provider using custom UserService
    public DaoAuthenticationProvider daoAuthenticationProvider(UserService userService,
                                                               PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    @Bean // Main security configuration
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   DaoAuthenticationProvider daoProvider) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity (enable for production if needed)
            .cors(Customizer.withDefaults())
            .authenticationProvider(daoProvider)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
                .requestMatchers(HttpMethod.POST, "/user").permitAll() 
                .requestMatchers(HttpMethod.GET, "/login", "/logout").permitAll() 
                .requestMatchers(HttpMethod.GET, "/article", "/article/**").permitAll() 
                .anyRequest().authenticated() 
            )
            .httpBasic(Customizer.withDefaults()); // Use basic authentication

        return http.build();
    }

    @Bean // Global CORS configuration
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of("http://localhost:5173"));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization","Content-Type","Accept","Origin","X-Requested-With"));
        cfg.setExposedHeaders(List.of("Authorization"));
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
