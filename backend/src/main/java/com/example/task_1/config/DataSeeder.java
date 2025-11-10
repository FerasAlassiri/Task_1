package com.example.task_1.config;

import com.example.task_1.entity.Privilege;
import com.example.task_1.entity.User;
import com.example.task_1.repository.PrivilegeRepository;
import com.example.task_1.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration 
public class DataSeeder {

    @Bean 
    CommandLineRunner seedDefaults(PrivilegeRepository privs, UserRepository users, PasswordEncoder encoder) {
        return args -> {
            // Ensure default privileges exist
            Privilege admin = privs.findByName("ADMIN").orElseGet(() -> {
                Privilege p = new Privilege(); p.setName("ADMIN"); return privs.save(p);
            });
            Privilege user = privs.findByName("USER").orElseGet(() -> {
                Privilege p = new Privilege(); p.setName("USER"); return privs.save(p);
            });

            // Create default admin user if not present
            if (!users.existsByUsername("admin")) {
                User u = new User();
                u.setUsername("admin");
                u.setEmail("admin@example.com");
                u.setMobileNumber("+10000000000");
                u.setPassword(encoder.encode("admin12345"));
                Set<Privilege> set = new HashSet<>(); set.add(admin); set.add(user);
                u.setPrivileges(set);
                users.save(u);
                System.out.println("[DataSeeder] Created admin: admin / admin12345");
            }
        };
    }
}
