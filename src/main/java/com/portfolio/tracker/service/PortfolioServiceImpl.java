package com.portfolio.tracker.service;

import com.portfolio.tracker.entity.Portfolio;
import com.portfolio.tracker.entity.User;
import com.portfolio.tracker.exception.ResourceNotFoundException;
import com.portfolio.tracker.repository.PortfolioRepository;
import com.portfolio.tracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    public PortfolioServiceImpl(PortfolioRepository portfolioRepository, UserRepository userRepository) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Portfolio create(Portfolio portfolio, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        portfolio.setUser(user);
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        log.info("Created portfolio with id {} for user {}", savedPortfolio.getId(), userId);
        return savedPortfolio;
    }

    @Override
    public Portfolio findById(Long id) {
        return portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found"));
    }

    @Override
    public List<Portfolio> findByUserId(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        return portfolioRepository.findByUserId(userId);
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
