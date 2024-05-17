package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.ShopService;
import org.example.domain.Shop;
import org.example.dto.response.NearbyShopInfoResponse;
import org.example.dto.response.ShopInfoResponse;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/")
    public List<NearbyShopInfoResponse> findNearbyShop(@RequestParam("latitude") String latitude,
                                                       @RequestParam("longitude") String longitude,
                                                       @RequestParam("radius") String radius) {
        return shopService.getShopsByCoordinate(latitude, longitude, radius);
    }
}
