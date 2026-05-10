package com.manjo.paymentgateway.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmountDto {
    @NotBlank
    private String value;
    private String currency = "IDR";
}
