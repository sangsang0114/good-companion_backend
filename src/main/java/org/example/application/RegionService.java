package org.example.application;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.domain.Region;
import org.example.infrastructure.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RegionService {
    private final RegionRepository shopRegionRepository;

    public List<Region> listRegions() {
        return shopRegionRepository.findAll();
    }

    public String getRegionNameByRegionId(String regionId) {
        Region shopRegion = shopRegionRepository.findById(regionId).orElseThrow(EntityNotFoundException::new);
        return shopRegion.getName();
    }
}
