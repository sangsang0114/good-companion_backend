package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.ShopManager;

@Getter
@Builder
public class ShopManagerResponse {
    private Long id;
    private String shopId;
    private String shopName;
    private String shopAddress;
    private String email;
    private String nickname;

    public static ShopManagerResponse toDto(ShopManager shopManager) {
        return ShopManagerResponse.builder()
                .id(shopManager.getId())
                .shopId(shopManager.getShop().getId())
                .shopName(shopManager.getShop().getName())
                .shopAddress(shopManager.getShop().getAddress())
                .email(shopManager.getMember().getEmail())
                .nickname(shopManager.getMember().getNickname())
                .build();
    }

}
