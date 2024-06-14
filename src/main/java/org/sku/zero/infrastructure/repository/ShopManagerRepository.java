package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.Member;
import org.sku.zero.domain.Shop;
import org.sku.zero.domain.ShopManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopManagerRepository extends JpaRepository<ShopManager, Long> {
    Boolean existsByShopAndMember(Shop shop, Member member);

    @Query("SELECT sm FROM ShopManager sm JOIN FETCH sm.shop WHERE sm.member = :member")
    List<ShopManager> findByMember(Member member);
}
