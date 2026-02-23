package com.portfolio.tracker.dto.asset.response;

import java.time.LocalDate;

public record BondResponseDTO(
        Long id,
        String symbol,
        Float quantity,
        Float buyPrice,
        Long portfolioId,
        Float couponRate,
        LocalDate maturityDate
) implements AssetResponseDTO {}
