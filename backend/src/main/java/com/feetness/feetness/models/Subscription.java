package com.feetness.feetness.models;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation OneToOne avec Customer
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonManagedReference
        private Customer customer;

    // Relation ManyToOne avec Pack
    @ManyToOne(optional = false)
    @JoinColumn(name = "pack_id", nullable = false)
 @JsonManagedReference
     private Pack pack;
    
    private LocalDate startDate;

    private LocalDate endDate;
}