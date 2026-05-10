package com.manjo.paymentgateway.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String responseCode;
    private String responseMessage;
    private String errorDetail;
    private T data;

    public static <T> ApiResponse<T> error(String code, String message) {
        return ApiResponse.<T>builder()
                .responseCode(code)
                .responseMessage(message)
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message, String detail) {
        return ApiResponse.<T>builder()
                .responseCode(code)
                .responseMessage(message)
                .errorDetail(detail)
                .build();
    }
}
