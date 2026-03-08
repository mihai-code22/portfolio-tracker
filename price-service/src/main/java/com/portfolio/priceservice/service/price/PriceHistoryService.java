package com.portfolio.priceservice.service.price;

import com.portfolio.priceservice.dto.PriceHistoryDTO;
import com.portfolio.priceservice.repository.mongo.PriceHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;

    public PriceHistoryService(PriceHistoryRepository priceHistoryRepository) {
        this.priceHistoryRepository = priceHistoryRepository;
    }

    public List<PriceHistoryDTO> findBySymbol(String symbol, LocalDateTime from, LocalDateTime to) {
        return priceHistoryRepository
                .findBySymbolAndTimestampBetweenOrderByTimestampAsc(symbol, from, to)
                .stream()
                .map(ph -> new PriceHistoryDTO(ph.getSymbol(), ph.getPrice(), ph.getTimestamp()))
                .toList();
    }
}
