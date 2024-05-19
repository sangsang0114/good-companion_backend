package org.example.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.domain.Shop;

@Getter
@Builder
public class BestShopResponse {
    private String shopId;
    private String shopName;
    private String shopAddress;
    private String boast;
    private String sector;
    private String imgUrl;

    public static BestShopResponse toDto(Shop shop, String imgUrl) {
        return BestShopResponse.builder()
                .shopId(shop.getId())
                .shopName(shop.getName())
                .shopAddress(shop.getAddress())
                .boast(shop.getBoast())
                .sector(shop.getShopSector())
                .imgUrl(imgUrl)
                .build();
    }
}
