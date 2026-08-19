package com.armalora.user.controller;

import com.armalora.user.dto.ChangePasswordRequest;
import com.armalora.user.dto.UpdateUserRequest;
import com.armalora.user.dto.UserResponse;
import com.armalora.user.entity.User;
import com.armalora.user.repository.UserRepository;
import com.armalora.user.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(
            UserRepository userRepository,
            UserService userService
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return ResponseEntity.ok(
                UserResponse.fromUser(user)
        );
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(
            Authentication authentication,
            @Valid @RequestBody UpdateUserRequest request
    ) {

        String currentEmail = authentication.getName();

        User updatedUser = userService.updateCurrentUser(
                currentEmail,
                request
        );

        return ResponseEntity.ok(
                UserResponse.fromUser(updatedUser)
        );
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request
    ) {

        String email = authentication.getName();

        userService.changePassword(
                email,
                request
        );

        return ResponseEntity.noContent().build();
    }
}