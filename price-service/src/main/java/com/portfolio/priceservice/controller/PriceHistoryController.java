package com.portfolio.priceservice.controller;

import com.portfolio.priceservice.dto.PriceHistoryDTO;
import com.portfolio.priceservice.service.price.PriceHistoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/prices")
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    public PriceHistoryController(PriceHistoryService priceHistoryService) {
        this.priceHistoryService = priceHistoryService;
    }

    @GetMapping("/{symbol}/history")
    public List<PriceHistoryDTO> getHistory(
            @PathVariable String symbol,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        LocalDateTime now = LocalDateTime.now();
        if (from == null) from = now.minusHours(1);
        if (to == null) to = now;
        return priceHistoryService.findBySymbol(symbol, from, to);
    }
}
