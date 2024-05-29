package org.sku.zero.dto.request;

import lombok.Builder;
import org.sku.zero.domain.Product;
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