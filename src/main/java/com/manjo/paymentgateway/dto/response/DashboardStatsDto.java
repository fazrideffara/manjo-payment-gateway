package com.manjo.paymentgateway.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
}
