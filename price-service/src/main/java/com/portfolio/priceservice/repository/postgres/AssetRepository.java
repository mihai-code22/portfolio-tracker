package com.portfolio.priceservice.repository.postgres;

import com.portfolio.priceservice.entity.postgres.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
}
