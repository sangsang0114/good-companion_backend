package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.RegionService;
import org.example.domain.Region;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/region")
public class RegionController {
    private final RegionService shopRegionService;

    @GetMapping("/")
    public List<Region> listRegions() {
        return shopRegionService.listRegions();
    }

    @GetMapping("/{regionId}")
    public String findNameRegionById(@PathVariable String regionId) {
        return shopRegionService.getRegionNameByRegionId(regionId);
    }
}