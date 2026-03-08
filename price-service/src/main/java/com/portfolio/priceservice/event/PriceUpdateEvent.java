package com.portfolio.priceservice.event;

import java.time.LocalDateTime;

public record PriceUpdateEvent(
        String symbol,
        Float price,
        LocalDateTime timestamp,
        String assetType
) {}
