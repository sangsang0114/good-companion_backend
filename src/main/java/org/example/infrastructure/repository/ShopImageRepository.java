package org.example.infrastructure.repository;

import org.example.domain.ShopImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopImageRepository extends JpaRepository<ShopImage, Integer> {
    Optional<ShopImage> findByShopId(String id);
}