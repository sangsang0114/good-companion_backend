package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.FranchiseService;
import org.example.domain.Franchise;
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
        System.out.println(zipcode);
        System.out.println(shopName);
        return ResponseEntity.status(HttpStatus.OK)
                .body(franchiseService.getFranchiseByZipCodeAndShopName(zipcode, shopName));
    }
}
