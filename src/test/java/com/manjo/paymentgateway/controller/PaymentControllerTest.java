package com.manjo.paymentgateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manjo.paymentgateway.constant.ApiEndpoints;
import com.manjo.paymentgateway.constant.MessageConstants;
import com.manjo.paymentgateway.dto.AmountDto;
import com.manjo.paymentgateway.dto.request.GenerateQrRequest;
import com.manjo.paymentgateway.dto.response.GenerateQrResponse;
import com.manjo.paymentgateway.service.JwtService;
import com.manjo.paymentgateway.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false) // Matikan security filter untuk unit test controller
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private JwtService jwtService; // Diperlukan oleh Spring Security context jika ada

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void generateQr_Success() throws Exception {
        GenerateQrRequest request = new GenerateQrRequest();
        request.setMerchantId("M1");
        request.setPartnerReferenceNo("P123");
        request.setAmount(new AmountDto("50000", "IDR"));

        GenerateQrResponse response = GenerateQrResponse.builder()
                .responseCode(MessageConstants.CODE_GENERATE_SUCCESS)
                .responseMessage("Successful")
                .qrContent("dummy-qr-data")
                .build();

        when(paymentService.createTransaction(any(), anyString())).thenReturn(response);

        mockMvc.perform(post(ApiEndpoints.QR_BASE + ApiEndpoints.QR_GENERATE)
                .header("X-Signature", "dummy-sig")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(MessageConstants.CODE_GENERATE_SUCCESS))
                .andExpect(jsonPath("$.qrContent").value("dummy-qr-data"));
    }
}
