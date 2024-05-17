package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.ShopService;
import org.example.domain.Shop;
import org.example.dto.response.ShopInfoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop")
public class ShopController {
    private final ShopService shopService;

    @GetMapping("/region/{regionCode}")
    public List<ShopInfoResponse> findShopsByRegionCode(@PathVariable String regionCode) {
        List<Shop> shops = shopService.getShopsByRegionCode(regionCode);
        return shops.stream().map(ShopInfoResponse::new).toList();
    }
}
