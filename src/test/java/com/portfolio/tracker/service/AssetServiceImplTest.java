package com.portfolio.tracker.service;

import com.portfolio.tracker.dto.asset.AssetMapper;
import com.portfolio.tracker.dto.asset.request.StockRequestDTO;
import com.portfolio.tracker.dto.asset.response.AssetResponseDTO;
import com.portfolio.tracker.dto.asset.response.StockResponseDTO;
import com.portfolio.tracker.entity.Portfolio;
import com.portfolio.tracker.entity.User;
import com.portfolio.tracker.entity.assets.Stock;
import com.portfolio.tracker.enums.Sector;
import com.portfolio.tracker.exception.ResourceNotFoundException;
import com.portfolio.tracker.repository.AssetRepository;
import com.portfolio.tracker.repository.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceImplTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private AssetMapper assetMapper;

    @InjectMocks
    private AssetServiceImpl assetService;

    private Portfolio portfolio;
    private Stock stock;
    private StockRequestDTO stockRequestDTO;
    private StockResponseDTO stockResponseDTO;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .id(1L)
                .username("johndoe")
                .build();

        portfolio = Portfolio.builder()
                .id(10L)
                .name("My Portfolio")
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        stock = Stock.builder()
                .id(100L)
                .symbol("AAPL")
                .quantity(5.0f)
                .buyPrice(150.0f)
                .exchange("NASDAQ")
                .sector(Sector.INFORMATION_TECHNOLOGY)
                .portfolio(portfolio)
                .build();

        stockRequestDTO = new StockRequestDTO("AAPL", 5.0f, 150.0f, "NASDAQ", Sector.INFORMATION_TECHNOLOGY);
        stockResponseDTO = new StockResponseDTO(100L, "AAPL", 5.0f, 150.0f, 10L, "NASDAQ", Sector.INFORMATION_TECHNOLOGY, "STOCK");
    }

    @Test
    void create_shouldSaveAssetAndReturnDTO() {
        when(portfolioRepository.findById(10L)).thenReturn(Optional.of(portfolio));
        when(assetMapper.toEntity(stockRequestDTO)).thenReturn(stock);
        when(assetRepository.save(stock)).thenReturn(stock);
        when(assetMapper.toDto(stock)).thenReturn(stockResponseDTO);

        AssetResponseDTO result = assetService.create(stockRequestDTO, 10L);

        assertThat(result).isEqualTo(stockResponseDTO);
        verify(assetRepository).save(stock);
        assertThat(stock.getPortfolio()).isEqualTo(portfolio);
    }

    @Test
    void create_shouldThrowResourceNotFoundException_whenPortfolioNotFound() {
        when(portfolioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assetService.create(stockRequestDTO, 99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Portfolio not found");

        verify(assetRepository, never()).save(any());
    }

    @Test
    void findById_shouldReturnDTO_whenAssetExists() {
        when(assetRepository.findById(100L)).thenReturn(Optional.of(stock));
        when(assetMapper.toDto(stock)).thenReturn(stockResponseDTO);

        AssetResponseDTO result = assetService.findById(100L);

        assertThat(result).isEqualTo(stockResponseDTO);
    }

    @Test
    void findById_shouldThrowResourceNotFoundException_whenAssetNotFound() {
        when(assetRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assetService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Asset not found");
    }

    @Test
    void findByPortfolioId_shouldReturnDTOList_whenPortfolioExists() {
        when(portfolioRepository.findById(10L)).thenReturn(Optional.of(portfolio));
        when(assetRepository.findByPortfolioId(10L)).thenReturn(List.of(stock));
        when(assetMapper.toDto(stock)).thenReturn(stockResponseDTO);

        List<AssetResponseDTO> result = assetService.findByPortfolioId(10L);

        assertThat(result).hasSize(1).containsExactly(stockResponseDTO);
    }

    @Test
    void findByPortfolioId_shouldThrowResourceNotFoundException_whenPortfolioNotFound() {
        when(portfolioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assetService.findByPortfolioId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Portfolio not found");

        verify(assetRepository, never()).findByPortfolioId(any());
    }

    @Test
    void delete_shouldDeleteAsset_whenAssetExists() {
        when(assetRepository.findById(100L)).thenReturn(Optional.of(stock));

        assetService.delete(100L);

        verify(assetRepository).delete(stock);
    }

    @Test
    void delete_shouldThrowResourceNotFoundException_whenAssetNotFound() {
        when(assetRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assetService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Asset not found");

        verify(assetRepository, never()).delete(any());
    }
}
