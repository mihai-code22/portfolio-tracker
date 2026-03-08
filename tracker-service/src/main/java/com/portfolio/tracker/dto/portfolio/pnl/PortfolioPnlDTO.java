package com.portfolio.tracker.dto.portfolio.pnl;

public record PortfolioPnlDTO(
        Long id,
        String name,
        Float totalInvested,
        Float currentValue,
        Float pnl,
        Float pnlPercentage
) {}