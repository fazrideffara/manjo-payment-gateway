package com.manjo.paymentgateway.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private String totalRevenue;
    private long activeTransactions;
    private String successRate;
    private String settlementVolume;
    private long totalTransactions;
    
    // Untuk Channel Performance (QRIS, Bank, CC)
    private Map<String, String> channelPerformance;
}
