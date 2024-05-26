package org.example.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.domain.Shop;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class ShopPageResponse {
    private List<ShopListResponse> shops;
    private int totalPages;
    private int currentPage;
    private boolean hasPrevious;
    private boolean isFirstPage;
    private boolean isLastPage;
    private long totalElements;

    public static ShopPageResponse toDto(List<ShopListResponse> shops, Page<Shop> shopPage) {
        return ShopPageResponse.builder()
                .shops(shops)
                .totalPages(shopPage.getTotalPages())
                .currentPage(shopPage.getNumber())
                .hasPrevious(shopPage.hasPrevious())
                .isFirstPage(shopPage.isFirst())
                .isLastPage(shopPage.isLast())
                .totalElements(shopPage.getTotalElements())
                .build();
    }
}
