package com.portfolio.tracker.dto;

public record UserRequestDTO(
        String username,
        String password,
        String email
) {}