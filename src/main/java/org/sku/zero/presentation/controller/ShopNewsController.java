package org.sku.zero.presentation.controller;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.sku.zero.application.ShopNewsService;
import org.sku.zero.domain.ShopNews;
import org.sku.zero.dto.request.AddShopNewsRequest;
import org.sku.zero.dto.response.ShopNewsPageResponse;
import org.sku.zero.dto.response.ShopNewsResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shop-news")
public class ShopNewsController {
    private final ShopNewsService shopNewsService;
    private final Faker faker;

    @GetMapping("/")
    public ResponseEntity<List<ShopNews>> getShopNews() {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/feed")
    public ResponseEntity<ShopNewsPageResponse> getShopNewsForFeed(
            @RequestParam(defaultValue = "0") Integer page,
            Principal principal) {
        Page<ShopNews> shopNewsPage = shopNewsService.findNewsByMarkedShops(10, page, principal);
        List<ShopNewsResponse> shopNewsResponses = shopNewsPage.stream().map(ShopNewsResponse::toDto).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ShopNewsPageResponse.toDto(shopNewsResponses, shopNewsPage));
    }

    @PostMapping("/")
    public ResponseEntity<Long> createShopNews(@RequestBody AddShopNewsRequest addShopNewsRequest, Principal principal) {
        Long id = shopNewsService.addShopNews(addShopNewsRequest, principal);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(id);
    }

    @PostMapping("/test")
    public ResponseEntity<List<Long>> createTestNews(Principal principal) {
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AddShopNewsRequest addShopNewsRequest = new AddShopNewsRequest("11647211", faker.lorem().sentence(), faker.lorem().sentence(), null);
            ids.add(shopNewsService.addShopNews(addShopNewsRequest, principal));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ids);
    }
}
