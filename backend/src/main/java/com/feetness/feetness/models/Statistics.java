package com.feetness.feetness.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Statistics {
    private long userCount;
    private long customerCount;
    private long packCount;
    private long subscriptionCount;
    // private long activeUserCount; // Nombre d'utilisateurs actifs
    // private long inactiveUserCount; 
    // private long totalRevenue; // Revenus totaux
}