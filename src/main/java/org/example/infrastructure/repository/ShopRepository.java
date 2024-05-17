package org.example.infrastructure.repository;

import org.example.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, String> {
    List<Shop> findShopsByShopRegion(String region);
}