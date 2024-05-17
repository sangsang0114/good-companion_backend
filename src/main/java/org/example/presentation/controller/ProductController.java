package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.ProductService;
import org.example.domain.Product;
import org.example.dto.request.AddProductRequest;
import org.example.dto.response.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/new")
    public ResponseEntity<Long> createProduct(@RequestBody AddProductRequest addProductRequest) {
        Long id = productService.addProduct(addProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(id);
    }
}
