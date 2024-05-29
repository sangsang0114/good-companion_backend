package org.sku.zero.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.sku.zero.application.FranchiseService;
import org.sku.zero.domain.Franchise;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/franchise")
@RestController
@RequiredArgsConstructor
public class FranchiseController {
    private final FranchiseService franchiseService;

    @GetMapping("/")
    public ResponseEntity<Franchise> findFranchiseByZipcodeAndShopName(
            @RequestParam String zipcode,
            @RequestParam String shopName
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(franchiseService.getFranchiseByZipCodeAndShopName(zipcode, shopName));
    }
}
