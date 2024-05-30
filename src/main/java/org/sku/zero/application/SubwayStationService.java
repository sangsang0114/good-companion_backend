package org.sku.zero.application;

import lombok.RequiredArgsConstructor;
import org.sku.zero.dto.response.SubwayStationResponse;
import org.sku.zero.infrastructure.repository.SubwayStationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubwayStationService {
    private final SubwayStationRepository subwayStationRepository;

    public List<SubwayStationResponse> getNearbySubwayStation(BigDecimal latitude, BigDecimal longitude, Double radius) {
        return subwayStationRepository.findStationsWithinRadiusWithDistance(latitude, longitude, radius);
    }
}
