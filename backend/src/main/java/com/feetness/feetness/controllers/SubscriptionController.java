package com.feetness.feetness.controllers;

import com.feetness.feetness.models.Customer;
import com.feetness.feetness.models.Pack;
import com.feetness.feetness.models.Subscription;
import com.feetness.feetness.repositories.CustomerRepo;
import com.feetness.feetness.repositories.PackRepo;
import com.feetness.feetness.repositories.SubscriptionRepo;
import com.feetness.feetness.requests.AddSubscriptionRequest;
import com.feetness.feetness.services.SubscriptionService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    @Autowired
    private SubscriptionRepo subscriptionRepository;

    @Autowired
    private CustomerRepo customerRepository;

    @Autowired
    private PackRepo packRepository;

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ResponseEntity<Response> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        Response response = Response.builder()
                .message("Subscriptions retrieved successfully")
                .data(subscriptions)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getSubscriptionById(@PathVariable Long id) {
        return subscriptionService.getSubscriptionById(id)
                .map(subscription -> {
                    Response response = Response.builder()
                            .message("Subscription retrieved successfully")
                            .data(subscription)
                            .build();
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.builder()
                                .message("Subscription not found")
                                .data(null)
                                .build()));
    }

    @PostMapping("/add")
    public ResponseEntity<Response> createSubscription(@Valid @RequestBody AddSubscriptionRequest request) {
        try {
            // Récupérer le Customer
            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Client non trouvé"));
    
            // Récupérer le Pack
            Pack pack = packRepository.findById(request.getPackId())
                    .orElseThrow(() -> new RuntimeException("Pack non trouvé"));
    
            // Vérifier si une souscription existe déjà pour ce client et ce pack
            Optional<Subscription> existingSubscription = subscriptionRepository.findByCustomerAndPack(customer, pack);
            if (existingSubscription.isPresent()) {
                throw new RuntimeException("Une souscription pour ce client et ce pack existe déjà.");
            }
    
            // Valider les dates
            if (request.getStartDate() == null || request.getEndDate() == null) {
                throw new RuntimeException("La date de début et la date de fin ne doivent pas être nulles.");
            }
            if (request.getStartDate().isAfter(request.getEndDate())) {
                throw new RuntimeException("La date de début doit être antérieure à la date de fin.");
            }
    
            // Construire l'objet Subscription
            Subscription subscription = new Subscription();
            subscription.setCustomer(customer);
            subscription.setPack(pack);
            subscription.setStartDate(request.getStartDate());
            subscription.setEndDate(request.getEndDate());
    
            // Sauvegarder et retourner la souscription créée
            Subscription savedSubscription = subscriptionRepository.save(subscription);
            Response response = Response.builder()
                    .message("Souscription créée avec succès")
                    .data(savedSubscription)
                    .build();
            return ResponseEntity.ok(response);
    
        } catch (RuntimeException e) {
            Response errorResponse = Response.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            Response errorResponse = Response.builder()
                    .message("Erreur interne du serveur")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    


    @PostMapping("/add2")
    public AddSubscriptionRequest createSubscription2(@Valid @RequestBody AddSubscriptionRequest subscription) {
            return subscription;        
    }
    @PostMapping("/test")
    public String test(){
        return "hello";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateSubscription(@PathVariable Long id, @RequestBody Subscription subscription) {
        Subscription updatedSubscription = subscriptionService.updateSubscription(id, subscription);
        Response response = Response.builder()
                .message("Subscription updated successfully")
                .data(updatedSubscription)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Response> deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        Response response = Response.builder()
                .message("Subscription deleted successfully")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}