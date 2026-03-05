package com.portfolio.priceservice.repository.mongo;

import com.portfolio.priceservice.entity.mongo.PriceHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceHistoryRepository extends MongoRepository<PriceHistory, String> {
    Optional<PriceHistory> findFirstBySymbolOrderByTimestampDesc(String symbol);
}
