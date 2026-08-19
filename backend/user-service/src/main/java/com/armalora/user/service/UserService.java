package com.armalora.user.service;

import com.armalora.user.dto.LoginResponse;
import com.armalora.user.dto.RegisterRequest;
import com.armalora.user.entity.User;
import com.armalora.user.entity.UserRole;
import com.armalora.user.exception.EmailAlreadyExistsException;
import com.armalora.user.exception.InvalidCredentialsException;
import com.armalora.user.repository.UserRepository;
import com.armalora.user.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // =========================
    // REGISTER USER
    // =========================
    public User registerUser(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new EmailAlreadyExistsException(
                    "User already exists with email: "
                            + request.getEmail()
            );
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        // Hash password before storing
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // Normal registration creates CUSTOMER
        user.setRole(UserRole.CUSTOMER);

        user.setActive(true);

        return userRepository.save(user);
    }

    // =========================
    // LOGIN USER
    // =========================
    public LoginResponse loginUser(
            String email,
            String password
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        )
                );

        // Check whether account is active
        if (!user.getActive()) {

            throw new InvalidCredentialsException(
                    "User account is inactive"
            );
        }

        // Check password
        if (!passwordEncoder.matches(
                password,
                user.getPassword()
        )) {

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        // Generate JWT
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        // Return user information + JWT
        return new LoginResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name(),
                token
        );
    }
}