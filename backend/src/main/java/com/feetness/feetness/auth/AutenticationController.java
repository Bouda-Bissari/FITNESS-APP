package com.feetness.feetness.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feetness.feetness.repositories.UserRepo;
import com.feetness.feetness.requests.UpdatePasswordRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AutenticationController {

    private final AuthenticationService authenticationService;
    private final UserRepo userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        
        if (userRepository.existsByEmail(request.getEmail())) {
            // Retourne une réponse avec un message d'erreur et un statut BAD_REQUEST
            AuthenticationResponse response = AuthenticationResponse.builder()
                .message("Ce email est déjà utilisé.")
                .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        
        AuthenticationResponse response = authenticationService.authenticate(request);
    
    if(response.getToken() == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
    @PostMapping("/update-password")
public ResponseEntity<AuthenticationResponse> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
    AuthenticationResponse response = authenticationService.updatePassword(request);
    
    // if (response.getToken() == null) {
    //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    // }
    
    return ResponseEntity.ok(response);
}

}
