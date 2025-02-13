package com.feetness.feetness.requests;


import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCustomerRequest {

    @NotNull(message = "Phone number cannot be null")
    @Size(min = 8, max = 15, message = "Phone number must be between 10 and 15 characters")
    @Pattern(regexp = "^\\+?[0-9]*$", message = "Phone number can only contain digits and optional '+' at the start")
    private String phoneNumber;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    private String lastName;

    @NotNull(message = "First name cannot be null")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    private String firstName;

    // @NotNull(message = "Registration date cannot be null")
    // @PastOrPresent(message = "Registration date must be today or in the past")
    private LocalDate registrationDate;

   
}
