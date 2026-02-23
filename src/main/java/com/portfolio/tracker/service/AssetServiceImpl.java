package com.portfolio.tracker.service;

import com.portfolio.tracker.entity.Asset;
import com.portfolio.tracker.entity.Portfolio;
import com.portfolio.tracker.exception.ResourceNotFoundException;
import com.portfolio.tracker.repository.AssetRepository;
import com.portfolio.tracker.repository.PortfolioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final PortfolioRepository portfolioRepository;

    public AssetServiceImpl(AssetRepository assetRepository, PortfolioRepository portfolioRepository) {
        this.assetRepository = assetRepository;
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    @Transactional
    public Asset create(Asset asset, Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found"));

        asset.setPortfolio(portfolio);
        Asset savedAsset = assetRepository.save(asset);
        log.info("Saved {} with id {}", savedAsset.getClass().getSimpleName(), savedAsset.getId());
        return savedAsset;
    }

    @Override
    public Asset findById(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
    }

    @Override
    public List<Asset> findByPortfolioId(Long portfolioId) {
        portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found"));

        return assetRepository.findByPortfolioId(portfolioId);
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
