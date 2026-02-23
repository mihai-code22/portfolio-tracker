package com.portfolio.tracker.service;

import com.portfolio.tracker.dto.asset.AssetMapper;
import com.portfolio.tracker.dto.asset.request.AssetRequestDTO;
import com.portfolio.tracker.dto.asset.response.AssetResponseDTO;
import com.portfolio.tracker.entity.Asset;
import com.portfolio.tracker.entity.Portfolio;
import com.portfolio.tracker.exception.ResourceNotFoundException;
import com.portfolio.tracker.repository.AssetRepository;
import com.portfolio.tracker.repository.PortfolioRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final PortfolioRepository portfolioRepository;
    private final AssetMapper assetMapper;

    public AssetServiceImpl(AssetRepository assetRepository, PortfolioRepository portfolioRepository, AssetMapper assetMapper) {
        this.assetRepository = assetRepository;
        this.portfolioRepository = portfolioRepository;
        this.assetMapper = assetMapper;
    }

    @Override
    @Transactional
    public AssetResponseDTO create(AssetRequestDTO assetRequestDTO, Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found"));

        Asset asset = assetMapper.toEntity(assetRequestDTO);
        asset.setPortfolio(portfolio);
        Asset savedAsset = assetRepository.save(asset);
        log.info("Saved {} with id {}", savedAsset.getClass().getSimpleName(), savedAsset.getId());
        return assetMapper.toDto(savedAsset);
    }

    @Override
    public AssetResponseDTO findById(Long id) {
        return assetRepository.findById(id).map(assetMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
    }

    @Override
    public List<AssetResponseDTO> findByPortfolioId(Long portfolioId) {
        portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found"));

        return assetRepository.findByPortfolioId(portfolioId).stream()
                .map(assetMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        assetRepository.delete(asset);
        log.info("Deleted asset with id {}", id);
    }
}
