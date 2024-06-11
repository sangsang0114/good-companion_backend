package org.sku.zero.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.sku.zero.application.ShopPendingService;
import org.sku.zero.domain.ShopPending;
import org.sku.zero.dto.response.ShopPendingPageResponse;
import org.sku.zero.dto.response.ShopPendingResponse;
import org.sku.zero.exception.ErrorCode;
import org.sku.zero.exception.NotFoundException;
import org.sku.zero.infrastructure.repository.ShopPendingRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/shop-pending/")
@RestController
public class ShopPendingController {
    private final ShopPendingService shopPendingService;
    private final ShopPendingRepository shopPendingRepository;

    @RequestMapping("/list")
    public ResponseEntity<ShopPendingPageResponse> listPendingShops(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page) {
        Page<ShopPending> shopPendingPage = shopPendingService.findAll(size, page);
        List<ShopPendingResponse> shopPendingPageResponses = shopPendingPage.getContent().stream()
                .map(ShopPendingResponse::toDto).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ShopPendingPageResponse.toDto(shopPendingPageResponses, shopPendingPage));
    }

    @RequestMapping("/{id}")
    public ResponseEntity<ShopPending> findPendingById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(shopPendingRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.SHOP_NOT_FOUND)));
    }
}
