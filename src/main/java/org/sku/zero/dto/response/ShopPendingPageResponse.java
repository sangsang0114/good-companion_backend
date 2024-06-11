package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.ShopPending;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class ShopPendingPageResponse {
    private List<ShopPendingResponse> shops;
    private int totalPages;
    private int currentPage;
    private boolean hasPrevious;
    private boolean isFirstPage;
    private boolean isLastPage;
    private long totalElements;

    public static ShopPendingPageResponse toDto(List<ShopPendingResponse> shops, Page<ShopPending> shopPendingPage) {
        return ShopPendingPageResponse.builder()
                .shops(shops)
                .totalPages(shopPendingPage.getTotalPages())
                .currentPage(shopPendingPage.getNumber())
                .hasPrevious(shopPendingPage.hasPrevious())
                .isFirstPage(shopPendingPage.isFirst())
                .isLastPage(shopPendingPage.isLast())
                .totalElements(shopPendingPage.getTotalElements())
                .build();
    }
}
