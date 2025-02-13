package com.feetness.feetness.auth;

import java.time.LocalDate;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.feetness.feetness.config.JwtService;
import com.feetness.feetness.models.Role;
import com.feetness.feetness.models.User;
import com.feetness.feetness.repositories.UserRepo;
import com.feetness.feetness.requests.UpdatePasswordRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse uniqueEmail(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthenticationResponse.builder()
                    .message("Cet email exist deja")
                    .build();
        }
        return null;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthenticationResponse.builder()
                    .message("Cet email est deja utilise")
                    .build();
        }
        var user = User.builder()
                .username(request.getUsername()) 
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) 
                .role(Role.USER) 
                .createdAt(LocalDate.now()) 
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("User registered successfully")
                .user(user)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
        } catch (BadCredentialsException e) {
            return AuthenticationResponse.builder()
                    .message("Identifiants incorrects")
                    .build();
        }
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("User authenticated successfully")
                .user(user)
                .build();
    }


    public AuthenticationResponse updatePassword(UpdatePasswordRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    // Vérifier le mot de passe actuel
    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
        throw new IllegalArgumentException("Current password is incorrect");
        
    }

    // Mettre à jour le mot de passe
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);

    // Créer une réponse
    return AuthenticationResponse.builder()
        .message("Password updated successfully.")
        .build();
}

}
