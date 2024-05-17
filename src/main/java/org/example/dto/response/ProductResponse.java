package org.example.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.Product;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProductResponse {
    private String name;
    private Integer price;

    public static ProductResponse toDto(Product product) {
        return ProductResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
