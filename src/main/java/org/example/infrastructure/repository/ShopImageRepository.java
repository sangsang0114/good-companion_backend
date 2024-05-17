package org.example.infrastructure.repository;

import org.example.domain.ShopImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopImageRepository extends JpaRepository<ShopImage, Integer> {
}