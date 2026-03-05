package com.portfolio.priceservice.entity.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "price_history")
public class PriceHistory {

    @Id
    private String id;
    private String symbol;
    private Float price;
    private LocalDateTime timestamp;

    public PriceHistory(String symbol, Float price, LocalDateTime timestamp) {
        this.symbol = symbol;
        this.price = price;
        this.timestamp = timestamp;
    }
}
