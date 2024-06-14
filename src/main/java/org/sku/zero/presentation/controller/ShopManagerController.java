package org.sku.zero.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.sku.zero.application.ShopManagerService;
import org.sku.zero.dto.response.ShopManagerResponse;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
