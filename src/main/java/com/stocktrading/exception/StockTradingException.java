package com.stocktrading.exception;

public class StockTradingException extends RuntimeException {
    
    private String errorCode;
    
    public StockTradingException(String message) {
        super(message);
    }
    
    public StockTradingException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public StockTradingException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public StockTradingException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
