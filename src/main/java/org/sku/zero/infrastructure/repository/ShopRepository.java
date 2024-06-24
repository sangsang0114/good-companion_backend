package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.Shop;
import org.sku.zero.dto.response.ShopCountsByRegionResponse;
import org.sku.zero.dto.response.ShopCountsBySectorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    Page<Shop> getShopsByNameContaining(String search, Pageable pageable);

    @Query("SELECT s " +
            "FROM Shop s " +
            "WHERE (:sector IS NULL OR s.shopSector = :sector) " +
            "AND (:region IS NULL OR s.shopRegion = :region) ")
    Page<Shop> findShopsBySectorAndRegion(String sector, String region, Pageable pageable);

    @Query("SELECT new org.sku.zero.dto.response.ShopCountsByRegionResponse(s.shopRegion, COUNT(s.id)) FROM Shop s GROUP BY s.shopRegion")
    List<ShopCountsByRegionResponse> countShopsByRegion();

    @Query("SELECT new org.sku.zero.dto.response.ShopCountsBySectorResponse(s.shopSector, COUNT(s.id)) FROM Shop s GROUP BY s.shopSector")
    List<ShopCountsBySectorResponse> countShopsBySector();


    @Query("SELECT new org.sku.zero.dto.response.ShopCountsBySectorResponse(s.shopSector, COUNT(s.id)) FROM Shop s WHERE s.shopRegion = :regionId GROUP BY s.shopSector")
    List<ShopCountsBySectorResponse> countsShopsBySectorOfRegion(@Param("regionId") String regionId);

}