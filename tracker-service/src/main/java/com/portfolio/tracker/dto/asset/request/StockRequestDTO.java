package com.portfolio.tracker.dto.asset.request;

import com.portfolio.tracker.enums.Sector;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockRequestDTO(
        @NotBlank String symbol,
        @Positive Float quantity,
        @Positive Float buyPrice,
        @NotBlank String exchange,
        @NotNull Sector sector
) implements AssetRequestDTO {}
