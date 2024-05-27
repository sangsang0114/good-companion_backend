package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.ShopService;
import org.example.domain.Shop;
import org.example.dto.request.AddShopRequest;
import org.example.dto.request.ModifyShopRequest;
import org.example.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @GetMapping("/list")
    public ResponseEntity<ShopPageResponse> listShops(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page) {
        Page<Shop> shopPage = shopService.findAllShops(page, size);
        List<ShopListResponse> shopList = shopPage.getContent()
                .stream().map(ShopListResponse::toDto)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ShopPageResponse.toDto(shopList, shopPage));
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

    @PatchMapping("/edit")
    public ResponseEntity<Boolean> modifyShop(@ModelAttribute ModifyShopRequest shopRequest) throws IOException {
        shopService.editShop(shopRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(true);
    }
}
