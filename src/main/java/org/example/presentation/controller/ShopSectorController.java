package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.ShopSectorService;
import org.example.domain.ShopSector;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/sector")
public class ShopSectorController {
    private final ShopSectorService shopSectorService;

    @GetMapping("/{sectorId}")
    public ResponseEntity<String> findSectorNameById(@PathVariable String sectorId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(shopSectorService.getSectorNameByID(sectorId));
    }

    @GetMapping("/")
    public ResponseEntity<List<ShopSector>> listSectors() {
        List<ShopSector> shopSectors = shopSectorService.listSectors();
        return ResponseEntity.status(HttpStatus.OK)
                .body(shopSectors);
    }
}
