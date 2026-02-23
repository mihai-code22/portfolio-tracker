package com.portfolio.tracker.dto.asset.response;

public sealed interface AssetResponseDTO
    permits StockResponseDTO, BondResponseDTO, CryptoResponseDTO {

    Long id();
    String symbol();
    Float quantity();
    Float buyPrice();
    Long portfolioId();
}
