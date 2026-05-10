package com.manjo.paymentgateway.constant;

public final class ApiEndpoints {
    private ApiEndpoints() {}

    // Base API Version
    public static final String API_V1 = "/api/v1";

    // QR Specific (Original Requirements)
    public static final String QR_BASE = API_V1 + "/qr";
    public static final String QR_GENERATE = "/generate";
    public static final String QR_QUERY = "/query";
    public static final String QR_CANCEL = "/cancel";
    public static final String QR_PAYMENT = "/payment";

    // Payment Generic (Extended)
    public static final String PAYMENT_BASE = API_V1 + "/payment";
    public static final String PAYMENT_CREATE = "/create";
    public static final String PAYMENT_CALLBACK = "/callback";
    
    // Admin / Dashboard Endpoints
    public static final String ADMIN_BASE = API_V1 + "/admin";
    public static final String TRANSACTIONS = "/transactions";
}
