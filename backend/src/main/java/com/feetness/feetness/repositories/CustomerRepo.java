package com.feetness.feetness.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feetness.feetness.models.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    Optional<Customer> findByLastNameAndFirstName(String lastName, String firstName);
    Boolean existsByPhoneNumber(String phoneNumber);

}