package org.example.application;

import lombok.RequiredArgsConstructor;
import org.example.domain.Franchise;
import org.example.infrastructure.repository.FranchiseRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseService {
    private final FranchiseRepository franchiseRepository;

    public Franchise getFranchiseByZipCodeAndShopName(String zipCode, String shopName) {
        Franchise franchise = franchiseRepository.findFranchiseByZipcodeAndShopName(zipCode, shopName)
                .orElse(null);
        return franchise;
    }

    public boolean isFranchise(String zipCode, String shopName) {
        Franchise franchise = franchiseRepository.findFranchiseByZipcodeAndShopName(zipCode, shopName)
                .orElse(null);
        return franchise != null;
    }
}
