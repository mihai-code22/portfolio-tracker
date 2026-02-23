package com.portfolio.tracker.dto.asset.request;

import java.time.LocalDate;

public record BondRequestDTO(
        String symbol,
        Float quantity,
        Float buyPrice,
        Float couponRate,
        LocalDate maturityDate
) implements AssetRequestDTO {}
