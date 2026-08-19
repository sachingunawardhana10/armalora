package com.armalora.user.service;

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

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        // Normal registration creates CUSTOMER
        user.setRole(UserRole.CUSTOMER);

        user.setActive(true);

        return userRepository.save(user);
    }

    public User loginUser(
            String email,
            String password
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        )
                );

        if (!user.getActive()) {

            throw new InvalidCredentialsException(
                    "User account is inactive"
            );
        }

        if (!passwordEncoder.matches(
                password,
                user.getPassword()
        )) {

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        return user;
    }

    public String generateToken(User user) {

        return jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}