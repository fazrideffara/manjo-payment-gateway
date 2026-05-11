package com.manjo.paymentgateway.constant;

public class TransactionStatus {
    public static final String PENDING = "PENDING";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    public static final String CANCELLED = "CANCELLED";
    public static final String EXPIRED = "EXPIRED";
    
    private TransactionStatus() {
        // Private constructor to prevent instantiation
    }
}
