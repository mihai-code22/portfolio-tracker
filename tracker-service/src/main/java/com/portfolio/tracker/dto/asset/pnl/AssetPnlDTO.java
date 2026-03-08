package com.portfolio.tracker.dto.asset.pnl;

public record AssetPnlDTO(
        Long id,
        String symbol,
        Float quantity,
        Float buyPrice,
        Float currentPrice,
        Float pnl,
        Float pnlPercentage
) {}
