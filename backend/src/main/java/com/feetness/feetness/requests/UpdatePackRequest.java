package com.feetness.feetness.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePackRequest {

    @NotBlank(message = "Offer name must not be empty.")
    private String offerName;

    @Min(value = 1, message = "Duration must be at least 1 month.")
    private int durationMonths;

    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly price must be greater than zero.")
    private double monthlyPrice;

    
}