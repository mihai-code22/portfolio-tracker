package com.portfolio.tracker.controller;

import com.portfolio.tracker.dto.asset.request.AssetRequestDTO;
import com.portfolio.tracker.dto.asset.response.AssetResponseDTO;
import com.portfolio.tracker.service.AssetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping("/portfolio/{portfolioId}")
    public ResponseEntity<AssetResponseDTO> create(@RequestBody AssetRequestDTO assetRequestDTO,
                                                   @PathVariable Long portfolioId) {
        AssetResponseDTO assetResponseDTO = assetService.create(assetRequestDTO, portfolioId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assetResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetResponseDTO> get(@PathVariable Long id) {
        AssetResponseDTO assetResponseDTO = assetService.findById(id);
        return ResponseEntity.ok(assetResponseDTO);
    }

    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<List<AssetResponseDTO>> getByPortfolioId(@PathVariable Long portfolioId) {
        List<AssetResponseDTO> assets = assetService.findByPortfolioId(portfolioId);
        return ResponseEntity.ok(assets);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        assetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
