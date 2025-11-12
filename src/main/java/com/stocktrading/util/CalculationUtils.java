package com.stocktrading.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalculationUtils {

    private static final int DEFAULT_SCALE = 4;
    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_UP;

    public static BigDecimal calculatePercentageChange(BigDecimal oldValue, BigDecimal newValue) {
        if (oldValue == null || newValue == null || oldValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal change = newValue.subtract(oldValue);
        return change.divide(oldValue, DEFAULT_SCALE, DEFAULT_ROUNDING)
                    .multiply(new BigDecimal("100"));
    }

    public static BigDecimal calculateCompoundAnnualGrowthRate(BigDecimal beginningValue, 
                                                              BigDecimal endingValue, 
                                                              int numberOfYears) {
        if (beginningValue == null || endingValue == null || 
            beginningValue.compareTo(BigDecimal.ZERO) <= 0 || numberOfYears <= 0) {
            return BigDecimal.ZERO;
        }
        
        double ratio = endingValue.divide(beginningValue, DEFAULT_SCALE, DEFAULT_ROUNDING).doubleValue();
        double exponent = 1.0 / numberOfYears;
        double cagr = Math.pow(ratio, exponent) - 1.0;
        
        return new BigDecimal(cagr).multiply(new BigDecimal("100"))
                                   .setScale(2, DEFAULT_ROUNDING);
    }

    public static BigDecimal calculateMovingAverage(List<BigDecimal> values, int period) {
        if (values == null || values.isEmpty() || period <= 0 || period > values.size()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal sum = BigDecimal.ZERO;
        int startIndex = Math.max(0, values.size() - period);
        
        for (int i = startIndex; i < values.size(); i++) {
            sum = sum.add(values.get(i));
        }
        
        return sum.divide(new BigDecimal(period), DEFAULT_SCALE, DEFAULT_ROUNDING);
    }

    public static BigDecimal calculateStandardDeviation(List<BigDecimal> values) {
        if (values == null || values.size() < 2) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal mean = calculateMean(values);
        BigDecimal sumOfSquaredDifferences = BigDecimal.ZERO;
        
        for (BigDecimal value : values) {
            BigDecimal difference = value.subtract(mean);
            sumOfSquaredDifferences = sumOfSquaredDifferences.add(difference.multiply(difference));
        }
        
        BigDecimal variance = sumOfSquaredDifferences.divide(new BigDecimal(values.size() - 1), 
                                                           DEFAULT_SCALE, DEFAULT_ROUNDING);
        
        return new BigDecimal(Math.sqrt(variance.doubleValue()))
                  .setScale(DEFAULT_SCALE, DEFAULT_ROUNDING);
    }

    public static BigDecimal calculateMean(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal sum = values.stream()
                              .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return sum.divide(new BigDecimal(values.size()), DEFAULT_SCALE, DEFAULT_ROUNDING);
    }

    public static BigDecimal calculateSharpeRatio(BigDecimal portfolioReturn, 
                                                  BigDecimal riskFreeRate, 
                                                  BigDecimal standardDeviation) {
        if (standardDeviation == null || standardDeviation.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal excessReturn = portfolioReturn.subtract(riskFreeRate);
        return excessReturn.divide(standardDeviation, DEFAULT_SCALE, DEFAULT_ROUNDING);
    }

    public static BigDecimal calculateBeta(List<BigDecimal> stockReturns, List<BigDecimal> marketReturns) {
        if (stockReturns == null || marketReturns == null || 
            stockReturns.size() != marketReturns.size() || stockReturns.size() < 2) {
            return BigDecimal.ONE; // Default beta
        }
        
        BigDecimal stockMean = calculateMean(stockReturns);
        BigDecimal marketMean = calculateMean(marketReturns);
        
        BigDecimal covariance = BigDecimal.ZERO;
        BigDecimal marketVariance = BigDecimal.ZERO;
        
        for (int i = 0; i < stockReturns.size(); i++) {
            BigDecimal stockDiff = stockReturns.get(i).subtract(stockMean);
            BigDecimal marketDiff = marketReturns.get(i).subtract(marketMean);
            
            covariance = covariance.add(stockDiff.multiply(marketDiff));
            marketVariance = marketVariance.add(marketDiff.multiply(marketDiff));
        }
        
        if (marketVariance.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ONE;
        }
        
        return covariance.divide(marketVariance, DEFAULT_SCALE, DEFAULT_ROUNDING);
    }

    public static BigDecimal calculateRSI(List<BigDecimal> prices, int period) {
        if (prices == null || prices.size() < period + 1) {
            return new BigDecimal("50"); // Neutral RSI
        }
        
        BigDecimal gains = BigDecimal.ZERO;
        BigDecimal losses = BigDecimal.ZERO;
        
        for (int i = 1; i <= period; i++) {
            BigDecimal change = prices.get(i).subtract(prices.get(i - 1));
            if (change.compareTo(BigDecimal.ZERO) > 0) {
                gains = gains.add(change);
            } else {
                losses = losses.add(change.abs());
            }
        }
        
        BigDecimal avgGain = gains.divide(new BigDecimal(period), DEFAULT_SCALE, DEFAULT_ROUNDING);
        BigDecimal avgLoss = losses.divide(new BigDecimal(period), DEFAULT_SCALE, DEFAULT_ROUNDING);
        
        if (avgLoss.compareTo(BigDecimal.ZERO) == 0) {
            return new BigDecimal("100");
        }
        
        BigDecimal rs = avgGain.divide(avgLoss, DEFAULT_SCALE, DEFAULT_ROUNDING);
        BigDecimal rsi = new BigDecimal("100").subtract(
            new BigDecimal("100").divide(BigDecimal.ONE.add(rs), DEFAULT_SCALE, DEFAULT_ROUNDING)
        );
        
        return rsi.setScale(2, DEFAULT_ROUNDING);
    }

    public static String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "$0.00";
        }
        return String.format("$%,.2f", amount);
    }

    public static String formatPercentage(BigDecimal percentage) {
        if (percentage == null) {
            return "0.00%";
        }
        return String.format("%.2f%%", percentage);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    public static BigDecimal roundToTwoDecimals(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value.setScale(2, DEFAULT_ROUNDING);
    }

    public static BigDecimal roundToFourDecimals(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value.setScale(4, DEFAULT_ROUNDING);
    }

    public static boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isNegative(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean isZero(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) == 0;
    }
}
