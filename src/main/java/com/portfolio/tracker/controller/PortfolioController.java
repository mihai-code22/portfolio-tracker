package com.portfolio.tracker.controller;

import com.portfolio.tracker.dto.portfolio.PortfolioRequestDTO;
import com.portfolio.tracker.dto.portfolio.PortfolioResponseDTO;
import com.portfolio.tracker.service.PortfolioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<PortfolioResponseDTO> createPortfolio(@RequestBody PortfolioRequestDTO portfolioRequestDTO,
                                                                @PathVariable Long userId) {
        PortfolioResponseDTO portfolioResponseDTO = portfolioService.create(portfolioRequestDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(portfolioResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioResponseDTO> getPortfolio(@PathVariable Long id) {
        PortfolioResponseDTO portfolioResponseDTO = portfolioService.findById(id);
        return ResponseEntity.ok(portfolioResponseDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PortfolioResponseDTO>> getPortfoliosByUserId(@PathVariable Long userId) {
        List<PortfolioResponseDTO> portfolios = portfolioService.findByUserId(userId);
        return ResponseEntity.ok(portfolios);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        portfolioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
