package com.feetness.feetness.controllers;

import com.feetness.feetness.models.Pack;
import com.feetness.feetness.requests.RegisterPackRequest;
import com.feetness.feetness.requests.UpdatePackRequest;
import com.feetness.feetness.services.PackService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packs")
@Validated
public class PackController {

    @Autowired
    private PackService packService;

    @GetMapping("/getAll")
    public ResponseEntity<Response> getAllPacks() {
        List<Pack> packs = packService.getAllPacks();
        Response response = Response.builder()
                .message("List of all packs retrieved successfully.")
                .data(packs)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getPackById(@PathVariable Long id) {
        return packService.getPackById(id)
                .map(pack -> {
                    Response response = Response.builder()
                            .message("Pack retrieved successfully.")
                            .data(pack)
                            .build();
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Response response = Response.builder()
                            .message("Pack not found.")
                            .data(null)
                            .build();
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @PostMapping("/add")
    public ResponseEntity<Response> createPack(@Valid @RequestBody RegisterPackRequest registerPackRequest) {
        Pack pack = new Pack();
        pack.setOfferName(registerPackRequest.getOfferName());
        pack.setDurationMonths(registerPackRequest.getDurationMonths());
        pack.setMonthlyPrice(registerPackRequest.getMonthlyPrice());
        
        Pack createdPack = packService.createPack(pack);
        Response response = Response.builder()
                .message("Pack created successfully.")
                .data(createdPack)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updatePack(@PathVariable Long id, @Valid @RequestBody UpdatePackRequest updatePackRequest) {
        Pack updatedPack = new Pack();
        updatedPack.setOfferName(updatePackRequest.getOfferName());
        updatedPack.setDurationMonths(updatePackRequest.getDurationMonths());
        updatedPack.setMonthlyPrice(updatePackRequest.getMonthlyPrice());
        
        Pack pack = packService.updatePack(id, updatedPack);
        Response response = Response.builder()
                .message("Pack updated successfully.")
                .data(pack)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deletePack(@PathVariable Long id) {
        packService.deletePack(id);
        Response response = Response.builder()
                .message("Pack deleted successfully.")
                .data(null)
                .build();
        return ResponseEntity.noContent().build();
    }
}