package com.portfolio.tracker.repository;

import com.portfolio.tracker.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findByPortfolioId(Long portfolioId);
}
