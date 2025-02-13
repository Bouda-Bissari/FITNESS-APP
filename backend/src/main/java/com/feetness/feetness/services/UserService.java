package com.feetness.feetness.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feetness.feetness.controllers.Response;
import com.feetness.feetness.models.User;
import com.feetness.feetness.repositories.UserRepo;
import com.feetness.feetness.requests.AddUserRequest;
import com.feetness.feetness.requests.UpdateUserRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    // Ajouter un utilisateur
    public Response addUser(AddUserRequest userRequest) {
        User userToSave = User.builder()
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .createdAt(LocalDate.now())
                .build();
        var userSaved = userRepo.save(userToSave);
        return Response.builder().message("User added successfully").data(userSaved).build();
    }

    // Récupérer un utilisateur par son ID
    public Response getUserById(Long id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            return Response.builder().message("User found").data(user.get()).build();
        } else {
            return Response.builder().message("User not found").build();
        }
    }

    // Récupérer tous les utilisateurs
    public Response getAllUsers() {
        List<User> users = userRepo.findAll();
        return Response.builder().message("Users fetched successfully").data(users).build();
    }

    // Mettre à jour un utilisateur
    public Response updateUser(Long id, UpdateUserRequest userDetails) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setUsername(userDetails.getUsername());
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setPassword(userDetails.getPassword());
            existingUser.setRole(userDetails.getRole());
            var updatedUser = userRepo.save(existingUser);
            return Response.builder().message("User updated successfully").data(updatedUser).build();
        } else {
            return Response.builder().message("User not found").build();
        }
    }

    // Supprimer un utilisateur
    public Response deleteUser(Long id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            userRepo.deleteById(id);
            return Response.builder().message("User deleted successfully").build();
        } else {
            return Response.builder().message("User not found").build();
        }
    }

    // Vérifier si un utilisateur existe par email
    public Response checkIfEmailExists(String email) {
        Boolean exists = userRepo.existsByEmail(email);
        if (exists) {
            return Response.builder().message("Email already exists").build();
        } else {
            return Response.builder().message("Email is available").build();
        }
    }
}
