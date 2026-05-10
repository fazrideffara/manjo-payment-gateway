package com.manjo.paymentgateway.exception;

public class SignatureInvalidException extends RuntimeException {
    public SignatureInvalidException(String message) {
        super(message);
    }
}
