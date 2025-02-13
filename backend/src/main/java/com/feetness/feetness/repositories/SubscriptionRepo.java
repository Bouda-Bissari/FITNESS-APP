package com.feetness.feetness.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.feetness.feetness.models.Customer;
import com.feetness.feetness.models.Pack;
import com.feetness.feetness.models.Subscription;

public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByCustomer(Customer customer);
    Optional<Subscription> findByPack(Pack pack);
    Optional<Subscription> findByCustomerAndPack(Customer customer, Pack pack);
    //  @EntityGraph(attributePaths = {"customer", "pack"})
    // List<Subscription> findAll();

   
}
