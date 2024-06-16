package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.Shop;
import org.sku.zero.domain.ShopNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopNewsRepository extends JpaRepository<ShopNews, Long> {
    @Query("SELECT n FROM ShopNews n JOIN FETCH n.shop JOIN FETCH n.member WHERE n.shop = :shop")
    Page<ShopNews> findShopNewsByShop(Shop shop, Pageable pageable);

    @Query("SELECT n FROM ShopNews n JOIN FETCH n.shop JOIN FETCH n.member WHERE n.shop.id IN :shops")
    Page<ShopNews> findShopNewsByMarkedShops(List<String> shops, Pageable pageable);

    Page<ShopNews> findShopNewsByShopIdIn(List<String> shopIds, Pageable pageable);
}
