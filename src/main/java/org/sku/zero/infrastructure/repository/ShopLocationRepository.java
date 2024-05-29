package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.ShopLocation;
import org.sku.zero.dto.response.NearbyShopInfoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopLocationRepository extends JpaRepository<ShopLocation, String> {
    @Query(value = "SELECT " +
            "new org.sku.zero.dto.response.NearbyShopInfoResponse(" +
            "sl.id, sl.shop.name, sl.shop.address, sl.shop.phone, sl.shop.rate, sl.shop.recommend, sl.latitude, sl.longitude, sl.shop.shopSector, sl.shop.imgUrlPublic) " +
            "FROM ShopLocation sl " +
            "JOIN sl.shop " +
            "WHERE 6371 * 2 * ASIN(SQRT(POWER(SIN((sl.latitude - :lat) * PI() / 180 / 2), 2) + " +
            "COS(:lat * PI() / 180) * COS(sl.latitude * PI() / 180) * " +
            "POWER(SIN((sl.longitude - :lng) * PI() / 180 / 2), 2))) <= :radius")
    List<NearbyShopInfoResponse> findNearbyShopLocations(@Param("lat") Double lat, @Param("lng") Double lng, @Param("radius") double radius);
}