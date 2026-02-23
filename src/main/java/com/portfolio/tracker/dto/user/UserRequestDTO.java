package com.portfolio.tracker.dto.user;

public record UserRequestDTO(
        String username,
        String password,
        String email
) {}