package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.Shop;

@Getter
@Builder
public class ShopManagerResponse {
    private String shopId;
    private String shopName;
    private String shopAddress;

    public static ShopManagerResponse toDto(Shop shop) {
        return ShopManagerResponse.builder()
                .shopId(shop.getId())
                .shopName(shop.getName())
                .shopAddress(shop.getAddress())
                .build();

    }
}
