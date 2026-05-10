package com.manjo.paymentgateway.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.manjo.paymentgateway.dto.AmountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponse {
    private String responseCode;
    private String responseMessage;
    private String referenceNo;
    private String partnerReferenceNo;
    private AmountDto amount;
    private String transactionStatusDesc;
}
