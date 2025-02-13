package com.feetness.feetness.requests;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddSubscriptionRequest {

    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be positive")
    private Long customerId;

    @NotNull(message = "Pack ID is required")
    @Positive(message = "Pack ID must be positive")
    private Long packId;

    @NotNull(message = "Start date is required")
    @Future(message = "Start date must be a future date")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be a future date")
    private LocalDate endDate;
}