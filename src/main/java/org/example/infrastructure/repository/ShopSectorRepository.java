package org.example.infrastructure.repository;

import org.example.domain.ShopSector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopSectorRepository extends JpaRepository<ShopSector, String> {
}
