package com.manjo.paymentgateway.dto.request;

import com.manjo.paymentgateway.dto.AmountDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GenerateQrRequest {
    @NotBlank(message = "partnerReferenceNo is required")
    private String partnerReferenceNo;

    @NotNull(message = "amount is required")
    @Valid
    private AmountDto amount;

    @NotBlank(message = "merchantId is required")
    private String merchantId;

    private String paymentMethod = "QR";

    private String callbackUrl;
    private String merchantName;
    private String customerUserNo;
    private String mpan;
    
    private String description;
}
