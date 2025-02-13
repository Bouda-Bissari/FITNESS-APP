package com.feetness.feetness.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.feetness.feetness.requests.AddCustomerRequest;
import com.feetness.feetness.requests.UpdateCustomerRequest;
import com.feetness.feetness.services.CustomerService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/add")
    public ResponseEntity<Response> addCustomer(@Valid @RequestBody AddCustomerRequest customer) {
        return ResponseEntity.ok(customerService.addCustomer(customer));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Response> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/getAll")
    public ResponseEntity<Response> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Response> updateCustomer(@Valid @RequestBody UpdateCustomerRequest customerDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customerDetails));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Response> deleteCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.deleteCustomer(id));
    }
}
