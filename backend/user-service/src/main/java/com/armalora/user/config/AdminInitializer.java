package com.armalora.user.config;

import com.armalora.user.entity.User;
import com.armalora.user.entity.UserRole;
import com.armalora.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {

            String adminEmail = "admin@armalora.com";

            if (userRepository.existsByEmail(adminEmail)) {
                return;
            }

            User admin = new User();

            admin.setFirstName("Armalora");
            admin.setLastName("Admin");
            admin.setEmail(adminEmail);

            admin.setPassword(
                    passwordEncoder.encode("Admin@123")
            );

            admin.setRole(UserRole.ADMIN);
            admin.setActive(true);

            userRepository.save(admin);

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Armalora ADMIN account created"
            );

            System.out.println(
                    "Email: " + adminEmail
            );

            System.out.println(
                    "===================================="
            );
        };
    }
}