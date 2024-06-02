package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.Shop;
import org.sku.zero.util.DatetimeUtil;

@Getter
@Builder
public class ShopListResponse {
    private String id;
    private String name;
    private String isAvailable;
    private Long recommend;
    private String updatedAt;

    public static ShopListResponse toDto(Shop shop) {
        return ShopListResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .isAvailable(shop.getIsAvailable() == 1 ? "지정" : "지정해제")
                .recommend(shop.getRecommend())
                .updatedAt(DatetimeUtil.formatDate(shop.getUpdatedAt()))
                .build();
    }
}