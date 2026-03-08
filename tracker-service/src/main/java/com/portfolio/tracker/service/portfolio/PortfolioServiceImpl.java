package com.portfolio.tracker.service.portfolio;

import com.portfolio.tracker.dto.portfolio.PortfolioMapper;
import com.portfolio.tracker.dto.portfolio.PortfolioRequestDTO;
import com.portfolio.tracker.dto.portfolio.PortfolioResponseDTO;
import com.portfolio.tracker.dto.portfolio.pnl.PortfolioPnlDTO;
import com.portfolio.tracker.entity.postgres.Portfolio;
import com.portfolio.tracker.entity.postgres.User;
import com.portfolio.tracker.exception.ResourceNotFoundException;
import com.portfolio.tracker.repository.postgres.AssetRepository;
import com.portfolio.tracker.repository.postgres.PortfolioRepository;
import com.portfolio.tracker.repository.postgres.UserRepository;
import com.portfolio.tracker.service.pnl.PnlCalculator;
import com.portfolio.tracker.service.price.PriceCache;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final PortfolioMapper portfolioMapper;
    private final AssetRepository assetRepository;
    private final PriceCache priceCache;
    private final PnlCalculator pnlCalculator;

    public PortfolioServiceImpl(PortfolioRepository portfolioRepository, UserRepository userRepository,
                                PortfolioMapper portfolioMapper, AssetRepository assetRepository,
                                PriceCache priceCache, PnlCalculator pnlCalculator) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
        this.portfolioMapper = portfolioMapper;
        this.assetRepository = assetRepository;
        this.priceCache = priceCache;
        this.pnlCalculator = pnlCalculator;
    }

    @Override
    @Transactional
    public PortfolioResponseDTO create(PortfolioRequestDTO portfolioRequestDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Portfolio portfolio = portfolioMapper.toEntity(portfolioRequestDTO);
        portfolio.setUser(user);
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        log.info("Created portfolio with id {} for user {}", savedPortfolio.getId(), userId);
        return portfolioMapper.toDto(savedPortfolio);
    }

    public PortfolioResponseDTO createForUsername(PortfolioRequestDTO dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Portfolio portfolio = portfolioMapper.toEntity(dto);
        portfolio.setUser(user);
        Portfolio saved = portfolioRepository.save(portfolio);
        return portfolioMapper.toDto(saved);
    }

    @Override
    public PortfolioResponseDTO findById(Long id) {
        return portfolioRepository.findById(id).map(portfolioMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found"));
    }

    @Override
    public List<PortfolioResponseDTO> findByUserId(Long userId, String username) throws AccessDeniedException {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        String portfolioUser = userOptional.get().getUsername();
        if (!username.equals(portfolioUser)) {
            throw new AccessDeniedException("Unable to delete other user's portfolio.");
        }

        return portfolioRepository.findByUserId(userId).stream()
                .map(portfolioMapper::toDto)
                .toList();
    }

    @Override
    public List<PortfolioResponseDTO> findByUsername(String username) {
        return portfolioRepository.findByUserUsername(username).stream()
                .map(portfolioMapper::toDto)
                .toList();
    }

    @Override
    public List<PortfolioPnlDTO> findByUsernameWithPnl(String username) {
        return portfolioRepository.findByUserUsername(username).stream()
                .map(portfolio -> {
                    var assetPnls = assetRepository.findByPortfolioId(portfolio.getId()).stream()
                            .map(asset -> pnlCalculator.forAsset(asset, priceCache.getCurrentPrice(asset.getSymbol())))
                            .toList();
                    return pnlCalculator.forPortfolio(portfolio, assetPnls);
                })
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id, String username) throws AccessDeniedException {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found"));

        String portfolioUser = portfolio.getUser().getUsername();
        if (!username.equals(portfolioUser)) {
            throw new AccessDeniedException("Unable to delete other user's portfolio.");
        }

        portfolioRepository.delete(portfolio);
        log.info("Deleted portfolio with id {}", id);
    }
}
