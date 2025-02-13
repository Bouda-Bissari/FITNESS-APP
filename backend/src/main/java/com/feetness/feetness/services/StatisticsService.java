package com.feetness.feetness.services;

import com.feetness.feetness.models.Statistics;
import com.feetness.feetness.repositories.UserRepo;
import com.feetness.feetness.repositories.CustomerRepo; 
import com.feetness.feetness.repositories.PackRepo; 
import com.feetness.feetness.repositories.SubscriptionRepo; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CustomerRepo customerRepo; 

    @Autowired
    private PackRepo packRepo; 

    @Autowired
    private SubscriptionRepo subscriptionRepo; 

    public Statistics getStatistics() {
        long userCount = userRepo.count();
        long customerCount = customerRepo.count();
        long packCount = packRepo.count();
        long subscriptionCount = subscriptionRepo.count();
       

        return Statistics.builder()
                .userCount(userCount)
                .customerCount(customerCount)
                .packCount(packCount)
                .subscriptionCount(subscriptionCount)
                .build();
    }
}