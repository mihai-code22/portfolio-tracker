package com.portfolio.tracker.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Sector {

    ENERGY("Energy"),
    MATERIALS("Materials"),
    INDUSTRIALS("Industrials"),
    CONSUMER_DISCRETIONARY("Consumer Discretionary"),
    CONSUMER_STAPLES("Consumer Staples"),
    HEALTH_CARE("Health Care"),
    FINANCIALS("Financials"),
    INFORMATION_TECHNOLOGY("Information Technology"),
    COMMUNICATION_SERVICES("Communication Services"),
    UTILITIES("Utilities"),
    REAL_ESTATE("Real Estate");

    private final String label;
}
