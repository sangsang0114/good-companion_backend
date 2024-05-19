package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.ShopService;
import org.example.domain.Shop;
import org.example.dto.request.AddShopRequest;
import org.example.dto.response.BestShopResponse;
import org.example.dto.response.NearbyShopInfoResponse;
import org.example.dto.response.ShopDetailResponse;
import org.example.dto.response.ShopInfoResponse;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @PostMapping("/register")
    public Boolean addNewShop(@ModelAttribute AddShopRequest addShopRequest) {
        shopService.addShop(addShopRequest);
        return true;
    }

    @GetMapping("/{shopId}")
    public ShopDetailResponse findShopById(@PathVariable String shopId) {
        ShopDetailResponse dto = shopService.getShopDetailByShopId(shopId);
        return dto;
    }

    @GetMapping("/best")
    public List<BestShopResponse> best() {
        return shopService.findBestRecommendedShopPerSector();
    }
}
