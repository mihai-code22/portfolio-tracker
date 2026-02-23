package com.portfolio.tracker.service;

import com.portfolio.tracker.dto.portfolio.PortfolioMapper;
import com.portfolio.tracker.dto.portfolio.PortfolioRequestDTO;
import com.portfolio.tracker.dto.portfolio.PortfolioResponseDTO;
import com.portfolio.tracker.entity.Portfolio;
import com.portfolio.tracker.entity.User;
import com.portfolio.tracker.exception.ResourceNotFoundException;
import com.portfolio.tracker.repository.PortfolioRepository;
import com.portfolio.tracker.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final PortfolioMapper portfolioMapper;

    public PortfolioServiceImpl(PortfolioRepository portfolioRepository, UserRepository userRepository, PortfolioMapper portfolioMapper) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
        this.portfolioMapper = portfolioMapper;
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

    @Override
    public PortfolioResponseDTO findById(Long id) {
        return portfolioRepository.findById(id).map(portfolioMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found"));
    }

    @Override
    public List<PortfolioResponseDTO> findByUserId(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        return portfolioRepository.findByUserId(userId).stream()
                .map(portfolioMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found"));

        portfolioRepository.delete(portfolio);
        log.info("Deleted portfolio with id {}", id);
    }
}
