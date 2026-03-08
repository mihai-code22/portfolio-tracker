package com.portfolio.tracker.event;

import java.time.LocalDateTime;

public record PriceUpdateEvent(
        String symbol,
        Float price,
        LocalDateTime timestamp,
        String assetType
) {}
