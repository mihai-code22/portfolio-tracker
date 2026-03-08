package com.portfolio.tracker.client;

import com.portfolio.tracker.dto.price.PriceHistoryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PriceHistoryClient {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final RestClient restClient;

    public PriceHistoryClient(@Value("${price-service.url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public List<PriceHistoryDTO> getPriceHistory(String symbol, LocalDateTime from, LocalDateTime to) {
        return restClient.get()
                .uri((UriBuilder builder) -> {
                    builder.path("/prices/{symbol}/history");
                    if (from != null) builder.queryParam("from", from.format(ISO));
                    if (to != null)   builder.queryParam("to",   to.format(ISO));
                    return builder.build(symbol);
                })
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
