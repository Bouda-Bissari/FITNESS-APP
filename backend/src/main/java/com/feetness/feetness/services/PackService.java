package com.feetness.feetness.services;
import com.feetness.feetness.models.Pack;
import com.feetness.feetness.repositories.PackRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PackService {

    @Autowired
    private PackRepo packRepository;

    public List<Pack> getAllPacks() {
        return packRepository.findAll();
    }

    public Optional<Pack> getPackById(Long id) {
        return packRepository.findById(id);
    }

    public Pack createPack(Pack pack) {
        return packRepository.save(pack);
    }

    public Pack updatePack(Long id, Pack packDetails) {
        Pack pack = packRepository.findById(id).orElseThrow();
        pack.setOfferName(packDetails.getOfferName());
        pack.setDurationMonths(packDetails.getDurationMonths());
        pack.setMonthlyPrice(packDetails.getMonthlyPrice());
        return packRepository.save(pack);
    }

    public void deletePack(Long id) {
        packRepository.deleteById(id);
    }
}