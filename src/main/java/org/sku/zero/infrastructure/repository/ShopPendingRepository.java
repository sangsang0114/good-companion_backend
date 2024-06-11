package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.ShopPending;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopPendingRepository extends JpaRepository<ShopPending, String> {
}
