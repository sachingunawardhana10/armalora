package com.armalora.user.service;

import com.armalora.user.dto.LoginResponse;
import com.armalora.user.dto.RegisterRequest;
import com.armalora.user.dto.UpdateProfileRequest;
import com.armalora.user.dto.UpdateUserRequest;
import com.armalora.user.entity.User;
import com.armalora.user.entity.UserRole;
import com.armalora.user.exception.EmailAlreadyExistsException;
import com.armalora.user.exception.InvalidCredentialsException;
import com.armalora.user.repository.UserRepository;
import com.armalora.user.security.JwtService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.armalora.user.dto.ChangePasswordRequest;

import java.util.List;

import com.armalora.user.exception.UserNotFoundException;

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

    public User updateCurrentUser(
            String currentEmail,
            UpdateUserRequest request
    ) {

        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "User not found"
                        )
                );

        if (!currentEmail.equalsIgnoreCase(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new EmailAlreadyExistsException(
                    "User already exists with email: "
                            + request.getEmail()
            );
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        return userRepository.save(user);
    }

    public void changePassword(
            String email,
            ChangePasswordRequest request
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "User not found"
                        )
                );

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword()
        )) {

            throw new InvalidCredentialsException(
                    "Current password is incorrect"
            );
        }

        if (!request.getNewPassword().equals(
                request.getConfirmPassword()
        )) {

            throw new IllegalArgumentException(
                    "New password and confirmation password do not match"
            );
        }

        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword()
        )) {

            throw new IllegalArgumentException(
                    "New password must be different from current password"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);
    }

    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "User not found"
                        )
                );
    }

    public User updateProfile(
            String email,
            UpdateProfileRequest request
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "User not found"
                        )
                );

        if (!user.getActive()) {
            throw new InvalidCredentialsException(
                    "User account is inactive"
            );
        }

        user.setFirstName(
                request.getFirstName()
        );

        user.setLastName(
                request.getLastName()
        );

        return userRepository.save(user);
    }

    public Page<User> getAllUsers(Pageable pageable) {

        return userRepository.findAll(pageable);
    }

    public Page<User> searchUsersByEmail(
            String email,
            Pageable pageable
    ) {

        return userRepository
                .findByEmailContainingIgnoreCase(
                        email,
                        pageable
                );
    }

    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + id
                        )
                );
    }

    public User deactivateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + id
                        )
                );

        user.setActive(false);

        return userRepository.save(user);
    }

    public User activateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + id
                        )
                );

        user.setActive(true);

        return userRepository.save(user);
    }

}