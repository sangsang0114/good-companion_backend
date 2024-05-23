package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.redis.DailyShopService;
import org.example.domain.redis.DailyShop;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/dailyShop")
@RequiredArgsConstructor
@RestController
public class DailyShopController {
    private final DailyShopService dailyShopService;

    @GetMapping("/")
    public ResponseEntity<List<DailyShop>> findDailyShop() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(dailyShopService.listDailyShop());
    }
}
