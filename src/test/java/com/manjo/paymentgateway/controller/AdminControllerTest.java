package com.manjo.paymentgateway.controller;

import com.manjo.paymentgateway.constant.ApiEndpoints;
import com.manjo.paymentgateway.dto.response.DashboardStatsDto;
import com.manjo.paymentgateway.repository.TransactionRepository;
import com.manjo.paymentgateway.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private JwtService jwtService;

    @Test
    void getStats_Success() throws Exception {
        DashboardStatsDto stats = DashboardStatsDto.builder()
                .totalRevenue("Rp 1.000.000")
                .activeTransactions(5)
                .successRate("100%")
                .build();

        // Di AdminController, getStats mengambil langsung dari repository
        // Tapi kita mock repository di integration test jika perlu.
        // Di sini kita hanya mengetest routing controller.
        
        mockMvc.perform(get(ApiEndpoints.ADMIN_BASE + ApiEndpoints.STATS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
