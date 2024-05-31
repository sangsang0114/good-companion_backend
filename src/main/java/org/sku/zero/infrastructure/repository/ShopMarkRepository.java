package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.Member;
import org.sku.zero.domain.Shop;
import org.sku.zero.domain.ShopMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShopMarkRepository extends JpaRepository<ShopMark, Long> {

    @Query("SELECT mark FROM ShopMark mark JOIN FETCH mark.shop WHERE mark.member =:member")
    List<ShopMark> findShopMarksByMember(Member member);

    Optional<ShopMark> findShopMarkByMemberAndShop(Member member, Shop shop);
}
