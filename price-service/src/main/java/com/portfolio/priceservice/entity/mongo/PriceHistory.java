package com.portfolio.priceservice.entity.mongo;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(collection = "price_history")
public class PriceHistory {

    @Id
    private String id;
    private final String symbol;
    private final Float price;
    private final LocalDateTime timestamp;

    public PriceHistory(String symbol, Float price, LocalDateTime timestamp) {
        this.symbol = symbol;
        this.price = price;
        this.timestamp = timestamp;
    }
}
