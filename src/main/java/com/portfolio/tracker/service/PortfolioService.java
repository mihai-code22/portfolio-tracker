package com.portfolio.tracker.service;

import com.portfolio.tracker.entity.Portfolio;

import java.util.List;

public interface PortfolioService {

    Portfolio create(Portfolio portfolio, Long userId);

    Portfolio findById(Long id);

    List<Portfolio> findByUserId(Long userId);

    void delete(Long id);
}
