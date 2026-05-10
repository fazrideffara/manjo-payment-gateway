package com.manjo.paymentgateway.constant;

public final class MessageConstants {
    private MessageConstants() {}

    public static final String SUCCESS = "Successful";
    public static final String INVALID_SIGNATURE = "Unauthorized: Invalid signature";
    public static final String TRANSACTION_NOT_FOUND = "Transaksi tidak ditemukan";
    public static final String BAD_REQUEST = "Permintaan tidak valid";
    public static final String CONFLICT = "Transaksi sudah ada";
    public static final String INTERNAL_SERVER_ERROR = "Terjadi kesalahan internal pada server";
    public static final String VALIDATION_ERROR = "Kesalahan validasi data";
    
    // Standard HTTP-like Status Codes
    public static final String CODE_SUCCESS = "200";
    public static final String CODE_UNAUTHORIZED = "401";
    public static final String CODE_NOT_FOUND = "404";
    public static final String CODE_BAD_REQUEST = "400";
    public static final String CODE_VALIDATION = "400"; // Alias ke 400
    public static final String CODE_CONFLICT = "409";
    public static final String CODE_INTERNAL_ERROR = "500";
}
