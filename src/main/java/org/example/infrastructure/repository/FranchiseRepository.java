package org.example.infrastructure.repository;

import org.example.domain.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FranchiseRepository extends JpaRepository<Franchise, Long> {
    Optional<Franchise> findFranchiseByZipcodeAndShopName(String zipcode, String shopName);
}
