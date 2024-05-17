package org.example.dto.request;

import lombok.Builder;
import org.example.domain.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record AddProductRequest(String shopId, String name, Integer price, List<MultipartFile> files) {
    @Builder
    public Product toEntity() {
        return Product.builder()
                .shopId(shopId)
                .name(name)
                .price(price)
                .build();
    }
}