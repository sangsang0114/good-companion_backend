package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.Member;
import org.sku.zero.domain.Review;
import org.sku.zero.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r JOIN FETCH r.member WHERE r.shop = :shop")
    List<Review> findReviewsByShop(Shop shop);

    List<Review> findTop3ByShopOrderByIdDesc(Shop shop);

    List<Review> findReviewsByShopAndMember(Shop shop, Member member);
}
