package com.portfolio.tracker.dto.portfolio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PortfolioRequestDTO(
        @NotBlank @Size(min = 3, max = 20) String name
) {}
