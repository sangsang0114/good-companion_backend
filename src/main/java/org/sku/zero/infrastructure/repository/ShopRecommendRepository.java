package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.ShopRecommend;
import org.sku.zero.dto.response.ListShopRecommendResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRecommendRepository extends JpaRepository<ShopRecommend, Long> {
    Optional<ShopRecommend> findByMemberIdAndShopId(Long memberId, String shopId);

    @Query("SELECT new org.sku.zero.dto.response.ListShopRecommendResponse(" +
            "sr.shop.id, sr.shop.name, sr.shop.address)" +
            "FROM ShopRecommend sr " +
            "JOIN sr.shop " +
            "WHERE sr.memberId = :memberId")
    List<ListShopRecommendResponse> findShopRecommendsByMemberId(Long memberId);
}
