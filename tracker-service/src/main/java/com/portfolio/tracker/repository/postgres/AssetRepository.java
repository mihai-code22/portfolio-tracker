package com.portfolio.tracker.repository.postgres;

import com.portfolio.tracker.entity.postgres.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findByPortfolioId(Long portfolioId);

    @Query("SELECT a FROM Asset a JOIN FETCH a.portfolio p JOIN FETCH p.user WHERE a.symbol = :symbol")
    List<Asset> findBySymbolWithPortfolioAndUser(String symbol);
}
