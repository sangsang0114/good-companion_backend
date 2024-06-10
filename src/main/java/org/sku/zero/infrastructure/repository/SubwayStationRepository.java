package org.sku.zero.infrastructure.repository;

import org.sku.zero.domain.SubwayStation;
import org.sku.zero.dto.response.SubwayStationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SubwayStationRepository extends JpaRepository<SubwayStation, String> {
    @Query("SELECT new org.sku.zero.dto.response.SubwayStationResponse(s.name, s.line, " +
            "6371 * 2 * ASIN(SQRT(POWER(SIN((:latitude - s.latitude) * PI() / 180 / 2), 2) + " +
            "COS(:latitude * PI() / 180) * COS(s.latitude * PI() / 180) * " +
            "POWER(SIN((:longitude - s.longitude) * PI() / 180 / 2), 2)))) " +
            "FROM SubwayStation s " +
            "WHERE 6371 * 2 * ASIN(SQRT(POWER(SIN((:latitude - s.latitude) * PI() / 180 / 2), 2) + " +
            "COS(:latitude * PI() / 180) * COS(s.latitude * PI() / 180) * " +
            "POWER(SIN((:longitude - s.longitude) * PI() / 180 / 2), 2))) <= :radius")
    List<SubwayStationResponse> findStationsWithinRadiusWithDistance(@Param("latitude") BigDecimal latitude,
                                                                     @Param("longitude") BigDecimal longitude,
                                                                     @Param("radius") double radius);


}
