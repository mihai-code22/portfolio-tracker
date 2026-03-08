package com.portfolio.tracker.controller;

import com.portfolio.tracker.dto.portfolio.PortfolioRequestDTO;
import com.portfolio.tracker.dto.portfolio.PortfolioResponseDTO;
import com.portfolio.tracker.dto.portfolio.pnl.PortfolioPnlDTO;
import com.portfolio.tracker.service.portfolio.PortfolioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
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

    @PostMapping("/me")
    public ResponseEntity<PortfolioResponseDTO> createMyPortfolio(
            @RequestBody PortfolioRequestDTO portfolioRequestDTO,
            @AuthenticationPrincipal String username) {
        PortfolioResponseDTO portfolioResponseDTO = portfolioService.createForUsername(portfolioRequestDTO, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(portfolioResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioResponseDTO> getPortfolio(@PathVariable Long id) {
        PortfolioResponseDTO portfolioResponseDTO = portfolioService.findById(id);
        return ResponseEntity.ok(portfolioResponseDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PortfolioResponseDTO>> getPortfoliosByUserId(@PathVariable Long userId,
                                                                            @AuthenticationPrincipal String username) throws AccessDeniedException {
        List<PortfolioResponseDTO> portfolios = portfolioService.findByUserId(userId, username);
        return ResponseEntity.ok(portfolios);
    }

    @GetMapping("/me")
    public ResponseEntity<List<PortfolioResponseDTO>> getMyPortfolios(
            @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(portfolioService.findByUsername(username));
    }

    @GetMapping("/me/pnl")
    public ResponseEntity<List<PortfolioPnlDTO>> getMyPortfoliosPnl(
            @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(portfolioService.findByUsernameWithPnl(username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal String username) throws AccessDeniedException {
        portfolioService.delete(id, username);
        return ResponseEntity.noContent().build();
    }
}
