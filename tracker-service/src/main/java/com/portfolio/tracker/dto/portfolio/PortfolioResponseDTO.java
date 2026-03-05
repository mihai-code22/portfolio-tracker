package com.portfolio.tracker.dto.portfolio;

import java.time.LocalDate;

public record PortfolioResponseDTO(
   Long id,
   String name,
   LocalDate createdAt,
   Long userId
) {}
