package com.armalora.user.controller;

import com.armalora.user.dto.LoginRequest;
import com.armalora.user.dto.LoginResponse;
import com.armalora.user.dto.RegisterRequest;
import com.armalora.user.dto.UserResponse;
import com.armalora.user.entity.User;
import com.armalora.user.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        User user =
                userService.registerUser(request);

        UserResponse response =
                UserResponse.fromUser(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        LoginResponse response =
                userService.loginUser(
                        request.getEmail(),
                        request.getPassword()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser(
            org.springframework.security.core.Authentication authentication
    ) {

        return ResponseEntity.ok(
                "Authenticated user: "
                        + authentication.getName()
        );
    }

}