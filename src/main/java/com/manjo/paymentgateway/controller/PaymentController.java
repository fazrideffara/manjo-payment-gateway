package com.manjo.paymentgateway.controller;

import com.manjo.paymentgateway.constant.ApiEndpoints;
import com.manjo.paymentgateway.constant.SwaggerConstants;
import com.manjo.paymentgateway.dto.request.GenerateQrRequest;
import com.manjo.paymentgateway.dto.request.PaymentCallbackRequest;
import com.manjo.paymentgateway.dto.response.GenerateQrResponse;
import com.manjo.paymentgateway.dto.response.PaymentResponse;
import com.manjo.paymentgateway.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @PostMapping(ApiEndpoints.QR_BASE + ApiEndpoints.QR_GENERATE)
    @Operation(summary = SwaggerConstants.GENERATE_QR_SUMMARY, description = SwaggerConstants.CREATE_PAYMENT_DESC)
    @SecurityRequirement(name = "X-Signature")
    public ResponseEntity<GenerateQrResponse> generateQr(
            @RequestHeader("X-Signature") String signature,
            @Valid @RequestBody GenerateQrRequest request) {
        request.setPaymentMethod("QR");
        GenerateQrResponse response = paymentService.createTransaction(request, signature);
        return ResponseEntity.ok(response);
    }

    @PostMapping(ApiEndpoints.QR_BASE + ApiEndpoints.QR_PAYMENT)
    @Operation(summary = SwaggerConstants.CALLBACK_QR_SUMMARY, description = SwaggerConstants.CALLBACK_PAYMENT_DESC)
    @SecurityRequirement(name = "X-Signature")
    public ResponseEntity<PaymentResponse> paymentCallback(
            @RequestHeader("X-Signature") String signature,
            @Valid @RequestBody PaymentCallbackRequest request) {
        log.info("Payment callback received: refNo={}, status={}", request.getOriginalReferenceNo(), request.getTransactionStatusDesc());
        PaymentResponse response = paymentService.processCallback(request, signature);
        return ResponseEntity.ok(response);
    }

    @GetMapping(ApiEndpoints.QR_BASE + ApiEndpoints.QR_QUERY)
    @Operation(summary = SwaggerConstants.QUERY_QR_SUMMARY, description = SwaggerConstants.QUERY_QR_DESC)
    public ResponseEntity<PaymentResponse> queryQr(
            @RequestParam(value = "partner_reference_no", required = false) String partnerReferenceNo,
            @RequestParam(value = "reference_number", required = false) String referenceNumber) {
        PaymentResponse response = paymentService.queryTransaction(partnerReferenceNo, referenceNumber);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping(ApiEndpoints.QR_BASE + ApiEndpoints.QR_CANCEL)
    @Operation(summary = SwaggerConstants.CANCEL_QR_SUMMARY, description = SwaggerConstants.CANCEL_QR_DESC)
    public ResponseEntity<PaymentResponse> cancelQr(
            @RequestParam("reference_number") String referenceNumber) {
        PaymentResponse response = paymentService.cancelTransaction(referenceNumber);
        return ResponseEntity.ok(response);
    }
}
