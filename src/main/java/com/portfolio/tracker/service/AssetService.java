package com.portfolio.tracker.service;

import com.portfolio.tracker.dto.asset.request.AssetRequestDTO;
import com.portfolio.tracker.dto.asset.response.AssetResponseDTO;

import java.util.List;

public interface AssetService {

    AssetResponseDTO create(AssetRequestDTO assetRequestDTO, Long portfolioId);

    AssetResponseDTO findById(Long id);

    List<AssetResponseDTO> findByPortfolioId(Long portfolioId);

    void delete(Long id);
}
