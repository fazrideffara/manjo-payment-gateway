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

    // Admin / Dashboard Endpoints
    public static final String ADMIN_BASE = API_V1 + "/admin";
    public static final String TRANSACTIONS = "/transactions";
    public static final String STATS = "/stats";

    // Auth Endpoints
    public static final String AUTH_BASE = API_V1 + "/auth";
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String REFRESH = "/refresh";
    public static final String LOGOUT = "/logout";
    public static final String PROFILE = "/profile";
}
