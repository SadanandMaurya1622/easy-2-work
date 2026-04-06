package com.easy2work.core.model;

import java.time.Instant;

/**
 * Represents a user in the Easy2Work system.
 * Users can be regular customers or administrators.
 */
public class User {
    private Long id;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String phone;
    private UserRole role;
    private Instant createdAt;
    private Instant lastLoginAt;

    public enum UserRole {
        CUSTOMER,
        ADMIN
    }

    public User() {
        this.createdAt = Instant.now();
        this.role = UserRole.CUSTOMER;
    }

    public User(String email, String passwordHash, String firstName, String lastName, String phone) {
        this();
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }
}
