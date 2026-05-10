package com.manjo.paymentgateway.constant;

public final class SwaggerConstants {
    private SwaggerConstants() {}

    public static final String API_TITLE = "Manjo Payment Gateway API";
    public static final String API_DESCRIPTION = "API Dokumentasi untuk sistem Payment Gateway PT Manjo Teknologi Indonesia. " +
            "Mendukung berbagai metode pembayaran termasuk QRIS, Bank Transfer, dan Credit Card.";
    public static final String API_VERSION = "v1.0.0";
    
    // Payment Controller
    public static final String PAYMENT_TAG = "Payment Management";
    public static final String PAYMENT_DESC = "Endpoint untuk manajemen transaksi pembayaran";
    
    public static final String CREATE_PAYMENT_SUMMARY = "Buat Transaksi Baru";
    public static final String CREATE_PAYMENT_DESC = "Endpoint untuk menginisialisasi transaksi baru (QRIS, Bank, CC). Membutuhkan validasi signature HMAC-SHA256.";
    
    public static final String CALLBACK_PAYMENT_SUMMARY = "Payment Callback (Webhook)";
    public static final String CALLBACK_PAYMENT_DESC = "Endpoint yang dipanggil oleh system partner/acquirer untuk memberikan notifikasi status pembayaran.";
}
