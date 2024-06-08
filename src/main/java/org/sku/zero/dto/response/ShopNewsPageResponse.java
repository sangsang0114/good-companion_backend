package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.ShopNews;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class ShopNewsPageResponse {
    private List<ShopNewsResponse> newsList;
    private int totalPages;
    private int currentPage;
    private boolean hasPrevious;
    private boolean isFirstPage;
    private boolean isLastPage;
    private long totalElements;

    public static ShopNewsPageResponse toDto(List<ShopNewsResponse> newsList, Page<ShopNews> newsPage) {
        return ShopNewsPageResponse.builder()
                .newsList(newsList)
                .totalPages(newsPage.getTotalPages())
                .currentPage(newsPage.getNumber())
                .hasPrevious(newsPage.hasPrevious())
                .isFirstPage(newsPage.isFirst())
                .isLastPage(newsPage.isLast())
                .totalElements(newsList.size())
                .build();
    }
}
