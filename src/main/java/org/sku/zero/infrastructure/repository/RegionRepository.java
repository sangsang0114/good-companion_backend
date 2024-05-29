package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, String> {
}
