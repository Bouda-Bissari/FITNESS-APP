package com.feetness.feetness.controllers;

import com.feetness.feetness.requests.AddUserRequest;
import com.feetness.feetness.requests.UpdateUserRequest;
import com.feetness.feetness.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Ajouter un nouvel utilisateur
    @PostMapping
    public ResponseEntity<Response> addUser(@RequestBody AddUserRequest userRequest) {
        Response response = userService.addUser(userRequest);
        return ResponseEntity.ok(response);
    }

    // Récupérer un utilisateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable Long id) {
        Response response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    // Récupérer tous les utilisateurs
    @GetMapping
    public ResponseEntity<Response> getAllUsers() {
        Response response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }

    // Mettre à jour un utilisateur
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest userDetails) {
        Response response = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(response);
    }

    // Supprimer un utilisateur
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable Long id) {
        Response response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }

    // Vérifier si un email existe
    @GetMapping("/check-email")
    public ResponseEntity<Response> checkIfEmailExists(@RequestParam String email) {
        Response response = userService.checkIfEmailExists(email);
        return ResponseEntity.ok(response);
    }
}