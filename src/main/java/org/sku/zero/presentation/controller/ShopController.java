package org.sku.zero.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.sku.zero.application.ShopService;
import org.sku.zero.domain.Shop;
import org.sku.zero.dto.request.AddShopRequest;
import org.sku.zero.dto.request.ModifyShopRequest;
import org.sku.zero.dto.response.*;
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
            @RequestParam(required = false) String sector,
            @RequestParam(required = false) String region,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page) {
        if ("null".equals(sector)) {
            sector = null;
        }
        if ("null".equals(region)) {
            region = null;
        }
        Page<Shop> shopPage = shopService.findShopsByRegionAndSector(sector, region, page, size);
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

    @GetMapping("/search")
    public ResponseEntity<ShopPageResponse> searchShops(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page) {
        Page<Shop> shopPage = shopService.getShopByNameKeyword(keyword, size, page);
        List<ShopListResponse> shopList = shopPage.getContent()
                .stream().map(ShopListResponse::toDto)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ShopPageResponse.toDto(shopList, shopPage));
    }

    @DeleteMapping("/unregister/{shopId}")
    public ResponseEntity<Boolean> unregisterShop(@PathVariable String shopId) {
        Boolean result = shopService.unregisterShop(shopId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @PatchMapping("/re-register/{shopId}")
    public ResponseEntity<Boolean> reRegisterShop(@PathVariable String shopId){
        Boolean result = shopService.reRegisterShop(shopId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/shop-counts-region")
    public List<ShopCountsByRegionResponse> getShopCountsByRegion() {
        return shopService.getShopCountByRegion();
    }

    @GetMapping("/shop-counts-sector")
    public List<ShopCountsBySectorResponse> getShopCountsBySector() {
        return shopService.getShopCountBySector();
    }
    @GetMapping("/shop-counts-sector-region")
    public List<ShopCountsBySectorResponse> getShopCountsBySectorOfRegion(@RequestParam String regionId) {
        return shopService.getShopCountBySectorOfRegion(regionId);
    }
}
