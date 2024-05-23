package org.example.infrastructure.repository;

import org.example.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, String> {
    List<Shop> findShopsByShopRegion(String region);

    @Query("SELECT s FROM Shop s " +
            "WHERE (s.shopSector, s.recommend) IN " +
            "(SELECT s2.shopSector, MAX(s2.recommend) FROM Shop s2 GROUP BY s2.shopSector) " +
            "AND s.id = (" +
            "    SELECT MIN(s3.id) FROM Shop s3 " +
            "    WHERE s3.shopSector = s.shopSector AND s3.recommend = s.recommend" +
            ") " +
            "ORDER BY s.shopSector, s.id")
    List<Shop> findBestRecommendedShopPerSector();

    @Query(value = "SELECT * FROM shop s ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Shop> getRandomThreeShops();
}