package com.portfolio.tracker.dto.asset.response;

public record CryptoResponseDTO(
        Long id,
        String symbol,
        Float quantity,
        Float buyPrice,
        Long portfolioId,
        String blockchain
) implements AssetResponseDTO {}
