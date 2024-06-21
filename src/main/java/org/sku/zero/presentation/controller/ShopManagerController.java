package org.sku.zero.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.sku.zero.application.ShopManagerService;
import org.sku.zero.dto.request.ShopManagerRequest;
import org.sku.zero.dto.response.ShopManagerResponse;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shop-manager")
public class ShopManagerController {
    private final ShopManagerService shopManagerService;
    private final ServerProperties serverProperties;

    @GetMapping("")
    public ResponseEntity<Boolean> findByShopIdAndMember(Principal principal, @RequestParam String shopId) {
        if (principal == null) return ResponseEntity.status(HttpStatus.OK).body(false);
        Boolean result = shopManagerService.existsByShopAndMember(shopId, principal);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/check-permissions")
    public ResponseEntity<List<ShopManagerResponse>> findByMember(Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.OK).body(List.of());
        return ResponseEntity.status(HttpStatus.OK)
                .body(shopManagerService.findByMember(principal));
    }

    @GetMapping("/list")
    public ResponseEntity<List<ShopManagerResponse>> listShopManager(
            @RequestParam(required = false) String shopId,
            @RequestParam(required = false) Long memberId) {
        List<ShopManagerResponse> responses = shopManagerService.findByShopOrMember(shopId, memberId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responses);
    }

    @PostMapping("")
    public ResponseEntity<Long> createShopManager(@RequestBody ShopManagerRequest shopManagerRequest) {
        Long savedId = shopManagerService.addShopManager(shopManagerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteShopManager(@PathVariable Long id) {
        Boolean result = shopManagerService.removeShopManager(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }
}
