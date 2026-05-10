package com.manjo.paymentgateway.dto.request;

import com.manjo.paymentgateway.dto.AmountDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentCallbackRequest {
    @NotBlank(message = "originalReferenceNo is required")
    private String originalReferenceNo;

    @NotBlank(message = "originalPartnerReferenceNo is required")
    private String originalPartnerReferenceNo;

    @NotBlank(message = "transactionStatusDesc is required")
    private String transactionStatusDesc;

    @NotBlank(message = "paidTime is required")
    private String paidTime;

    @NotNull(message = "amount is required")
    @Valid
    private AmountDto amount;
}
