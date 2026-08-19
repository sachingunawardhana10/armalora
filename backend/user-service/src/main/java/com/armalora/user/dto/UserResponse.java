package com.armalora.user.dto;

import com.armalora.user.entity.User;
import com.armalora.user.entity.UserRole;

import java.time.LocalDateTime;

public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private Boolean active;
    private LocalDateTime createdAt;

    public UserResponse() {
    }

    public static UserResponse fromUser(User user) {

        UserResponse response = new UserResponse();

        response.id = user.getId();
        response.firstName = user.getFirstName();
        response.lastName = user.getLastName();
        response.email = user.getEmail();
        response.role = user.getRole();
        response.active = user.getActive();
        response.createdAt = user.getCreatedAt();

        return response;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}