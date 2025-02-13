package com.feetness.feetness.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feetness.feetness.models.Pack;

public interface PackRepo extends JpaRepository<Pack, Long> {
    Optional<Pack> findByOfferName(String offerName);
    Optional<Pack> findByDurationMonths(int durationMonths);
    Optional<Pack> findByMonthlyPrice(double monthlyPrice);
    
}
