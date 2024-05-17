package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.ProductService;
import org.example.domain.Product;
import org.example.dto.response.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public ResponseEntity<List<ProductResponse>> findShopProductsByShopId(@RequestParam final String shopId) {
        List<Product> products = productService.getProductsByShopId(shopId);
        List<ProductResponse> lists = products.stream().map(product -> ProductResponse.toDto(product)).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(lists);
    }
}
