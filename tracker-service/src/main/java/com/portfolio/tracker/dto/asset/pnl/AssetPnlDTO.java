package com.portfolio.tracker.dto.asset.pnl;

import com.portfolio.common.enums.AssetType;

public record AssetPnlDTO(
        Long id,
        String symbol,
        Float quantity,
        Float buyPrice,
        Float currentPrice,
        Float pnl,
        Float pnlPercentage,
        AssetType type
) {}
