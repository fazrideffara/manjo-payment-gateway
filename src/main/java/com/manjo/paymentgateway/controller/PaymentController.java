package com.manjo.paymentgateway.controller;

import com.manjo.paymentgateway.constant.ApiEndpoints;
import com.manjo.paymentgateway.constant.SwaggerConstants;
import com.manjo.paymentgateway.dto.request.GenerateQrRequest;
import com.manjo.paymentgateway.dto.request.PaymentCallbackRequest;
import com.manjo.paymentgateway.dto.response.GenerateQrResponse;
import com.manjo.paymentgateway.dto.response.PaymentResponse;
import com.manjo.paymentgateway.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@Tag(name = SwaggerConstants.PAYMENT_TAG, description = SwaggerConstants.PAYMENT_DESC)
public class PaymentController {

    private final PaymentService paymentService;

    // Endpoint asli dari Soal Tes (QR)
    @PostMapping(ApiEndpoints.QR_BASE + ApiEndpoints.QR_GENERATE)
    @Operation(summary = "Generate QR MPM (Original)", description = SwaggerConstants.CREATE_PAYMENT_DESC)
    public ResponseEntity<GenerateQrResponse> generateQr(
            @RequestHeader("X-Signature") String signature,
            @Valid @RequestBody GenerateQrRequest request) {
        request.setPaymentMethod("QR");
        return createTransaction(signature, request);
    }

    @PostMapping(ApiEndpoints.QR_BASE + ApiEndpoints.QR_PAYMENT)
    @Operation(summary = "Payment Callback (Original)", description = SwaggerConstants.CALLBACK_PAYMENT_DESC)
    public ResponseEntity<PaymentResponse> paymentCallback(
            @RequestHeader("X-Signature") String signature,
            @Valid @RequestBody PaymentCallbackRequest request) {
        return processCallback(signature, request);
    }

    @GetMapping(ApiEndpoints.QR_BASE + ApiEndpoints.QR_QUERY)
    @Operation(summary = "Query Payment (Original)", description = "Query payment status by trx_id or reference_number")
    public ResponseEntity<PaymentResponse> queryQr(
            @RequestParam(value = "trx_id", required = false) String trxId,
            @RequestParam(value = "reference_number", required = false) String referenceNumber) {
        PaymentResponse response = paymentService.queryTransaction(trxId, referenceNumber);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping(ApiEndpoints.QR_BASE + ApiEndpoints.QR_CANCEL)
    @Operation(summary = "Cancel QR Payment (Original)", description = "Cancel a PENDING QR payment")
    public ResponseEntity<PaymentResponse> cancelQr(
            @RequestParam("reference_number") String referenceNumber) {
        PaymentResponse response = paymentService.cancelTransaction(referenceNumber);
        return ResponseEntity.ok(response);
    }

    // Generic Endpoints
    @PostMapping(ApiEndpoints.PAYMENT_BASE + ApiEndpoints.PAYMENT_CREATE)
    @Operation(summary = SwaggerConstants.CREATE_PAYMENT_SUMMARY, description = SwaggerConstants.CREATE_PAYMENT_DESC)
    public ResponseEntity<GenerateQrResponse> createTransaction(
            @RequestHeader("X-Signature") String signature,
            @Valid @RequestBody GenerateQrRequest request) {
        log.info("Create {} request: merchantId={}", request.getPaymentMethod(), request.getMerchantId());
        GenerateQrResponse response = paymentService.createTransaction(request, signature);
        return ResponseEntity.ok(response);
    }

    @PostMapping(ApiEndpoints.PAYMENT_BASE + ApiEndpoints.PAYMENT_CALLBACK)
    @Operation(summary = SwaggerConstants.CALLBACK_PAYMENT_SUMMARY, description = SwaggerConstants.CALLBACK_PAYMENT_DESC)
    public ResponseEntity<PaymentResponse> processCallback(
            @RequestHeader("X-Signature") String signature,
            @Valid @RequestBody PaymentCallbackRequest request) {
        log.info("Payment callback received: refNo={}, status={}", request.getOriginalReferenceNo(), request.getTransactionStatusDesc());
        PaymentResponse response = paymentService.processCallback(request, signature);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping(ApiEndpoints.PAYMENT_BASE + ApiEndpoints.PAYMENT_BANK)
    @Operation(summary = "Bank Transfer Placeholder")
    public ResponseEntity<String> bankTransfer() {
        return ResponseEntity.ok("Bank Transfer endpoint placeholder");
    }
    
    @PostMapping(ApiEndpoints.PAYMENT_BASE + ApiEndpoints.PAYMENT_CC)
    @Operation(summary = "Credit Card Placeholder")
    public ResponseEntity<String> creditCard() {
        return ResponseEntity.ok("Credit Card endpoint placeholder");
    }
}
