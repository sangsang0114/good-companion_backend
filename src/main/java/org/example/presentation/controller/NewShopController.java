package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.redis.NewShopService;
import org.example.domain.redis.NewShop;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/newShop")
public class NewShopController {
    private final NewShopService newShopService;

    @GetMapping("/")
    public ResponseEntity<List<NewShop>> listNewShops() {
        return ResponseEntity.status(HttpStatus.OK).body(newShopService.listNewShops());
    }
}
