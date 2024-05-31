package org.sku.zero.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.sku.zero.application.ShopMarkService;
import org.sku.zero.dto.request.ShopMarkRequest;
import org.sku.zero.dto.response.ShopMarkResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopmark")
public class ShopMarkController {
    private final ShopMarkService shopMarkService;

    @PostMapping("/")
    public ResponseEntity<Long> addShopMark(@RequestBody ShopMarkRequest shopMarkRequest, Principal principal) {
        Long savedId = shopMarkService.addShopMark(shopMarkRequest, principal);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedId);
    }

    @GetMapping("/")
    public ResponseEntity<List<ShopMarkResponse>> getShopMarksByMember(Principal principal) {
        List<ShopMarkResponse> responses = shopMarkService.getShopMarksByMember(principal);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responses);
    }

    @DeleteMapping("/")
    public ResponseEntity<Boolean> deleteShopMark(@RequestBody ShopMarkRequest shopMarkRequest, Principal principal) {
        Boolean result = shopMarkService.deleteShopMark(shopMarkRequest, principal);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isExists(Principal principal, @RequestParam String shopId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(shopMarkService.isExist(principal, shopId));
    }
}
