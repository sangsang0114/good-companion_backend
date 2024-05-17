package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.ShopLocationService;
import org.example.dto.response.ShopLocationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shopLocation/")
public class ShopLocationController {
    private final ShopLocationService shopLocationService;

    @GetMapping("/{shopId}")
    public ShopLocationResponse getShopLocation(@PathVariable("shopId") String shopId) {
        return shopLocationService.findByShopId(shopId);
    }
}