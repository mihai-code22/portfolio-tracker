package com.portfolio.tracker.controller;

import com.portfolio.tracker.client.PriceHistoryClient;
import com.portfolio.tracker.dto.price.PriceHistoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
public class PriceHistoryController {

    private final PriceHistoryClient priceHistoryClient;

    @GetMapping("/{symbol}/history")
    public ResponseEntity<List<PriceHistoryDTO>> getHistory(
            @PathVariable String symbol,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(priceHistoryClient.getPriceHistory(symbol, from, to));
    }
}
