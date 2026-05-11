package com.manjo.paymentgateway.constant;

public final class SwaggerConstants {
    private SwaggerConstants() {}

    public static final String API_TITLE = "Manjo Payment Gateway API";
    public static final String API_DESCRIPTION = "API Dokumentasi untuk sistem Payment Gateway PT Manjo Teknologi Indonesia. " +
            "Fokus utama pada integrasi QRIS (MPM) sesuai standar SNAP BI.";
    public static final String API_VERSION = "v1.0.0";
    
    // Payment Controller (QRIS)
    public static final String PAYMENT_TAG = "QRIS Management";
    public static final String PAYMENT_DESC = "Endpoint untuk manajemen transaksi QRIS";
    
    public static final String GENERATE_QR_SUMMARY = "Generate QR MPM";
    public static final String CREATE_PAYMENT_DESC = "Inisialisasi transaksi QRIS baru. Membutuhkan validasi signature HMAC-SHA256.";
    
    public static final String CALLBACK_QR_SUMMARY = "Payment Callback (Webhook)";
    public static final String CALLBACK_PAYMENT_DESC = "Notifikasi status pembayaran dari partner/acquirer.";

    public static final String QUERY_QR_SUMMARY = "Query Transaction Status";
    public static final String QUERY_QR_DESC = "Cek status transaksi menggunakan trx_id atau reference_number";
    public static final String CANCEL_QR_SUMMARY = "Cancel Transaction";
    public static final String CANCEL_QR_DESC = "Membatalkan transaksi yang masih berstatus PENDING";

    // Admin Controller
    public static final String ADMIN_TAG = "Admin Management";
    public static final String ADMIN_DESC = "Endpoints untuk monitoring transaksi dan manajemen dashboard";
    public static final String GET_ALL_TRANSACTIONS_SUMMARY = "Get All Transactions";
    public static final String GET_ALL_TRANSACTIONS_DESC = "Mengambil data transaksi secara terpagi (paginated) untuk dashboard admin";
    public static final String GET_STATS_SUMMARY = "Get Dashboard Stats";
    public static final String GET_STATS_DESC = "Menghitung statistik real-time untuk dashboard";

    // Auth Controller
    public static final String AUTH_TAG = "Authentication Management";
    public static final String AUTH_DESC = "Endpoints untuk login, register, dan manajemen profile";
    public static final String LOGIN_SUMMARY = "User Login";
    public static final String REGISTER_SUMMARY = "User Registration";
    public static final String REFRESH_SUMMARY = "Refresh JWT Token";
    public static final String LOGOUT_SUMMARY = "User Logout";
    public static final String PROFILE_SUMMARY = "Update User Profile";
}
