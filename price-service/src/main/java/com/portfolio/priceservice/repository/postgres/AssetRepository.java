package com.portfolio.priceservice.repository.postgres;

import com.portfolio.priceservice.entity.postgres.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    @Query("SELECT a FROM Asset a WHERE TYPE(a) = :type")
    List<Asset> findByType(@Param("type") Class<? extends Asset> type);
}
