package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.ShopImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopImage")
public class ShopImageController {
    private final ShopImageService shopImageService;

    @RequestMapping("/{shopId}")
    public ResponseEntity<String> findShopImageUrlByShopId(@PathVariable String shopId) {
        String url = shopImageService.getShopImageUrlByShopId(shopId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(url);
    }
}