package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.ShopMark;

@Getter
@Builder
public class ShopMarkResponse {
    private String shopId;
    private String shopName;
    private String shopAddress;
    private String shopPhone;

    public static ShopMarkResponse toDto(ShopMark shopMark) {
        return ShopMarkResponse.builder()
                .shopId(shopMark.getShop().getId())
                .shopName(shopMark.getShop().getName())
                .shopAddress(shopMark.getShop().getAddress())
                .shopPhone(shopMark.getShop().getPhone())
                .build();
    }
}
