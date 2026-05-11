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
    
    // Standard SNAP BI Status Codes
    public static final String CODE_SUCCESS = "2007300"; // General Success
    public static final String CODE_GENERATE_SUCCESS = "2004700";
    public static final String CODE_QUERY_SUCCESS = "2005100";
    public static final String CODE_NOTIFY_SUCCESS = "2005100";
    
    public static final String CODE_UNAUTHORIZED = "4017300";
    public static final String CODE_NOT_FOUND = "4047300";
    public static final String CODE_BAD_REQUEST = "4007300";
    public static final String CODE_VALIDATION = "4007301";
    public static final String CODE_CONFLICT = "4097300";
    public static final String CODE_INTERNAL_ERROR = "5007300";
}
