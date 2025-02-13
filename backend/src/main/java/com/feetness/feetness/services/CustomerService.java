package com.feetness.feetness.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.feetness.feetness.controllers.Response;
import com.feetness.feetness.models.Customer;
import com.feetness.feetness.models.Pack;
import com.feetness.feetness.models.Subscription;
import com.feetness.feetness.repositories.CustomerRepo;
import com.feetness.feetness.repositories.PackRepo;
import com.feetness.feetness.repositories.SubscriptionRepo;
import com.feetness.feetness.requests.AddCustomerRequest;
import com.feetness.feetness.requests.SubscriptionRequest;
import com.feetness.feetness.requests.UpdateCustomerRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private PackRepo packService;

    @Autowired
    private SubscriptionRepo subscriptionRepo;

    // Ajouter un client
    public Response addCustomer(AddCustomerRequest customer) {
        customer.setRegistrationDate(LocalDate.now());
        Customer customerToSave = Customer.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(customer.getPhoneNumber())
                .activeSubscription(false)
                .registrationDate(customer.getRegistrationDate())
                .build();
        var customerSave = customerRepo.save(customerToSave);
        return Response.builder().message("Customer added successfully").data(customerSave).build();
    }

    // Récupérer un client par son ID avec ses subscriptions
    public Response getCustomerById(Long id) {
        Optional<Customer> customer = customerRepo.findById(id);

        if (customer.isPresent()) {
            return Response.builder()
                    .message("Customer found")
                    .data(customer.get())
                    .build();
        } else {
            return Response.builder()
                    .message("Customer not found")
                    .build();
        }
    }

    // Récupérer tous les clients
    public Response getAllCustomers() {
        List<Customer> customers = customerRepo.findAll();
        return Response.builder()
                .message("Customers fetched successfully")
                .data(customers)
                .build();
    }


    public Response updateCustomer(Long id, UpdateCustomerRequest customerDetails) {
        if (id == null) {
            return Response.builder().message("Customer ID cannot be null").build();
        }
        if (customerDetails == null) {
            return Response.builder().message("Customer details cannot be null").build();
        }
    
        try {
            Optional<Customer> customerOptional = customerRepo.findById(id);
            if (customerOptional.isPresent()) {
                Customer existingCustomer = customerOptional.get();
    
                // Update basic customer information
                existingCustomer.setFirstName(customerDetails.getFirstName());
                existingCustomer.setLastName(customerDetails.getLastName());
                existingCustomer.setPhoneNumber(customerDetails.getPhoneNumber());
    
                // Check subscription details
                if (customerDetails.getSubscription() != null) {
                    SubscriptionRequest subscriptionRequest = customerDetails.getSubscription();
                    Subscription existingSubscription = existingCustomer.getSubscription();
    
                    // Update or create subscription
                    if (existingSubscription != null) {
                        existingSubscription.setPack(packService.findById(subscriptionRequest.getPackId())
                            .orElseThrow(() -> new RuntimeException("Pack not found")));
                        existingSubscription.setStartDate(subscriptionRequest.getStartDate());
                        existingSubscription.setEndDate(subscriptionRequest.getEndDate());
                    } else {
                        Subscription newSubscription = new Subscription();
                        newSubscription.setPack(packService.findById(subscriptionRequest.getPackId())
                            .orElseThrow(() -> new RuntimeException("Pack not found")));
                        newSubscription.setStartDate(subscriptionRequest.getStartDate());
                        newSubscription.setEndDate(subscriptionRequest.getEndDate());
                        newSubscription.setCustomer(existingCustomer); // Set the customer reference
                        existingCustomer.setSubscription(newSubscription);
                    }
                }
    
                Customer updatedCustomer = customerRepo.save(existingCustomer);
                return Response.builder()
                        .message("Customer updated successfully")
                        .data(updatedCustomer)
                        .build();
            } else {
                return Response.builder()
                        .message("Customer not found")
                        .build();
            }
        } catch (Exception e) {
            return Response.builder()
                    .message("An error occurred while updating the customer: " + e.getMessage())
                    .build();
        }
    }

 

    // Supprimer un client
    public Response deleteCustomer(Long id) {
        Optional<Customer> customer = customerRepo.findById(id);
        if (customer.isPresent()) {
            customerRepo.deleteById(id);
            return Response.builder().message("Customer deleted successfully").build();
        } else {
            return Response.builder().message("Customer not found").build();
        }
    }
}
