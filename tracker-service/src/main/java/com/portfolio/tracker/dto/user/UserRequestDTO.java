package com.portfolio.tracker.dto.user;

import com.portfolio.tracker.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Email String email,
        @NotBlank @ValidPassword String password
) {}