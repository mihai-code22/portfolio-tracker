package com.portfolio.tracker.dto.asset.response;

import com.portfolio.tracker.enums.Sector;

public record StockResponseDTO(
        Long id,
        String symbol,
        Float quantity,
        Float buyPrice,
        Long portfolioId,
        String exchange,
        Sector sector
) implements AssetResponseDTO {}
