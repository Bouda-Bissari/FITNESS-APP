package com.feetness.feetness.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.feetness.feetness.models.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Le nom d'utilisateur est requis")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit être entre 3 et 50 caractères")
    private String username; 

    @NotBlank(message = "L'email est requis")
    @Email(message = "L'email doit être valide")
    private String email; 

    @NotBlank(message = "Le mot de passe est requis")
    // @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    // @Pattern(
    //     regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
    //     message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial"
    // )
    private String password; 

    // @NotBlank(message = "Le rôle est requis")
    // private Role role; 

    

    // @NotBlank(message = "La date de création est requise")
    // private String createdAt; 
}