package org.example.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.ProductService;
import org.example.dto.request.AddProductRequest;
import org.example.dto.request.ModifyProductRequest;
import org.example.dto.response.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public ResponseEntity<List<ProductResponse>> findShopProductsByShopId(@RequestParam final String shopId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductsByShopId(shopId));
    }

    @PostMapping("/new")
    public ResponseEntity<Long> createProduct(@ModelAttribute AddProductRequest addProductRequest) {
        Long id = productService.addProduct(addProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(id);
    }

    @PatchMapping("/edit")
    public ResponseEntity<Long> modifyProduct(@ModelAttribute ModifyProductRequest modifyProductRequest) throws IOException {
        Long pId = productService.editProduct(modifyProductRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(pId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(true);
    }
}
