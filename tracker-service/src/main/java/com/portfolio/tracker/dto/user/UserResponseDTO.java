package com.portfolio.tracker.dto.user;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String username,
        String email,
        LocalDateTime createdAt
) {}