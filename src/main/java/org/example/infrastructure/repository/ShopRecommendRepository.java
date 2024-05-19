package org.example.infrastructure.repository;

import org.example.domain.ShopRecommend;
import org.example.dto.response.ListShopRecommendResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRecommendRepository extends JpaRepository<ShopRecommend, Long> {
    Optional<ShopRecommend> findByMemberIdAndShopId(Long memberId, String shopId);

    @Query("SELECT new org.example.dto.response.ListShopRecommendResponse(" +
            "sr.shop.id, sr.shop.name, sr.shop.address)" +
            "FROM ShopRecommend sr " +
            "JOIN sr.shop " +
            "WHERE sr.memberId = :memberId")
    List<ListShopRecommendResponse> findShopRecommendsByMemberId(Long memberId);
}
