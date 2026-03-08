package com.portfolio.tracker.dto.price;

import java.time.LocalDateTime;

public record PriceHistoryDTO(
        String symbol,
        Float price,
        LocalDateTime timestamp
) {}
