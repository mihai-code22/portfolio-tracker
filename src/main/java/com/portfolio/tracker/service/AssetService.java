package com.portfolio.tracker.service;

import com.portfolio.tracker.entity.Asset;

import java.util.List;

public interface AssetService {

    Asset create(Asset asset, Long portfolioId);

    Asset findById(Long id);

    List<Asset> findByPortfolioId(Long portfolioId);

    void delete(Long id);
}
