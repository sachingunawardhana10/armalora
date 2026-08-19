package com.armalora.user.controller;

import com.armalora.user.dto.UserResponse;
import com.armalora.user.entity.User;
import com.armalora.user.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserAdminController {

    private final UserService userService;

    public UserAdminController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @PageableDefault(
                    size = 10
            )
            Pageable pageable
    ) {

        Page<User> users =
                userService.getAllUsers(pageable);

        Page<UserResponse> response =
                users.map(UserResponse::fromUser);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserResponse>> searchUsers(
            @RequestParam String email,

            @PageableDefault(
                    size = 10
            )
            Pageable pageable
    ) {

        Page<User> users =
                userService.searchUsersByEmail(
                        email,
                        pageable
                );

        Page<UserResponse> response =
                users.map(UserResponse::fromUser);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id
    ) {

        User user =
                userService.getUserById(id);

        return ResponseEntity.ok(
                UserResponse.fromUser(user)
        );
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<UserResponse> deactivateUser(
            @PathVariable Long id
    ) {

        User user = userService.deactivateUser(id);

        return ResponseEntity.ok(
                UserResponse.fromUser(user)
        );
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<UserResponse> activateUser(
            @PathVariable Long id
    ) {

        User user = userService.activateUser(id);

        return ResponseEntity.ok(
                UserResponse.fromUser(user)
        );
    }
}