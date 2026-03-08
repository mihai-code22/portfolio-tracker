package com.portfolio.priceservice.repository.mongo;

import com.portfolio.priceservice.entity.mongo.PriceHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceHistoryRepository extends MongoRepository<PriceHistory, String> {
    Optional<PriceHistory> findFirstBySymbolOrderByTimestampDesc(String symbol);
    List<PriceHistory> findBySymbolAndTimestampBetweenOrderByTimestampAsc(String symbol, LocalDateTime from, LocalDateTime to);
}
