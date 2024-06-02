package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.Shop;

@Getter
@Builder
public class BestShopResponse {
    private String shopId;
    private String name;
    private String address;
    private String boast;
    private String sector;
    private String imgUrl;

    public static BestShopResponse toDto(Shop shop, String imgUrl) {
        return BestShopResponse.builder()
                .shopId(shop.getId())
                .name(shop.getName())
                .address(shop.getAddress())
                .boast(shop.getBoast())
                .sector(shop.getShopSector())
                .imgUrl(imgUrl)
                .build();
    }
}
