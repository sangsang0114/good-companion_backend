package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.ShopRecommendService;
import org.example.dto.request.ShopRecommendRequest;
import org.example.dto.response.ListShopRecommendResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopRecommend")
public class ShopRecommendController {
    private final ShopRecommendService shopRecommendService;

    @GetMapping("/check")
    public ResponseEntity<Boolean> isExists(Principal principal, @RequestParam String shopId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(shopRecommendService.isExist(principal, shopId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<ListShopRecommendResponse>> listShopRecommend(Principal principal) {
        List<ListShopRecommendResponse> shopRecommendServices = shopRecommendService.listShopRecommendResponses(principal);
        return ResponseEntity.status(HttpStatus.OK)
                .body(shopRecommendServices);
    }

    @PostMapping("/register")
    public ResponseEntity<Long> addShopRecommend(Principal principal, @RequestBody ShopRecommendRequest request) {
        Long id = shopRecommendService.addShopRecommend(principal, request.getShopId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(id);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Boolean> deleteShopRecommend(Principal principal, @RequestBody ShopRecommendRequest request) {
        shopRecommendService.deleteShopRecommend(principal, request.getShopId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(true);
    }
}
