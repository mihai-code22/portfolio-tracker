package com.portfolio.priceservice.dto;

import java.time.LocalDateTime;

public record PriceHistoryDTO(
        String symbol,
        Float price,
        LocalDateTime timestamp
) {}
