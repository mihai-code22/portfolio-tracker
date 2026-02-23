package com.portfolio.tracker.dto.asset.request;

import com.portfolio.tracker.enums.Sector;

public record StockRequestDTO(
        String symbol,
        Float quantity,
        Float buyPrice,
        String exchange,
        Sector sector
) implements AssetRequestDTO {}
