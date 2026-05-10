package com.manjo.paymentgateway.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateQrResponse {
    private String responseCode;
    private String responseMessage;
    private String referenceNo;
    private String partnerReferenceNo;
    private String qrContent;
}
