package com.manjo.paymentgateway.service;

import com.manjo.paymentgateway.dto.AmountDto;
import com.manjo.paymentgateway.dto.request.GenerateQrRequest;
import com.manjo.paymentgateway.dto.response.GenerateQrResponse;
import com.manjo.paymentgateway.exception.SignatureInvalidException;
import com.manjo.paymentgateway.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private SignatureService signatureService;

    @Mock
    private WebhookService webhookService;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransaction_Success() {
        // Arrange
        GenerateQrRequest request = new GenerateQrRequest();
        request.setMerchantId("M1");
        request.setPartnerReferenceNo("P1");
        request.setAmount(new AmountDto("100.00", "IDR"));
        
        when(signatureService.validateSignature(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true);
        when(transactionRepository.existsByTrxId(anyString())).thenReturn(false);

        // Act
        GenerateQrResponse response = paymentService.createTransaction(request, "valid-sig");

        // Assert
        assertNotNull(response);
        assertEquals("200", response.getResponseCode());
        verify(transactionRepository, times(1)).save(any());
        verify(webhookService, times(1)).sendNotification(any());
    }

    @Test
    void createTransaction_InvalidSignature_ThrowsException() {
        // Arrange
        GenerateQrRequest request = new GenerateQrRequest();
        request.setAmount(new AmountDto("100.00", "IDR"));
        when(signatureService.validateSignature(any(), any(), any(), any()))
                .thenReturn(false);

        // Act & Assert
        assertThrows(SignatureInvalidException.class, () -> 
            paymentService.createTransaction(request, "invalid-sig")
        );
    }
}
