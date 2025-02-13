package com.feetness.feetness.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerRequest {

    @NotBlank(message = "Le numéro de téléphone est requis")
    // @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Le numéro de téléphone doit être au format valide (10 chiffres, optionnellement précédé du code pays)")
    private String phoneNumber;

    @NotBlank(message = "Le nom de famille est requis")
    @Size(max = 100, message = "Le nom de famille ne doit pas dépasser 100 caractères")
    private String lastName;

    @NotBlank(message = "Le prénom est requis")
    @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères")
    private String firstName;

    private boolean isActiveSubscription;
    private SubscriptionRequest subscription;
}
