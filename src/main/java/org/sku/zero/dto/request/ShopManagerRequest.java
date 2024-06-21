package org.sku.zero.dto.request;

import org.sku.zero.domain.Member;
import org.sku.zero.domain.Shop;
import org.sku.zero.domain.ShopManager;

public record ShopManagerRequest(String shopId, Long memberId) {
    public ShopManager toEntity(Shop shop, Member member) {
        return ShopManager.builder()
                .shop(shop)
                .member(member)
                .build();
    }
}
