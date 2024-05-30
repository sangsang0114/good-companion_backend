package org.sku.zero.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.sku.zero.application.SubwayStationService;
import org.sku.zero.dto.response.SubwayStationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/subway")
public class SubwayStationController {
    private final SubwayStationService subwayStationService;

    @GetMapping("/")
    public ResponseEntity<List<SubwayStationResponse>> getSubwayStationsByLatitude(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude
    ) {
        List<SubwayStationResponse> subwayStationResponses = subwayStationService.getNearbySubwayStation(latitude, longitude, 0.5);
        return ResponseEntity.status(HttpStatus.OK)
                .body(subwayStationResponses);
    }
}
