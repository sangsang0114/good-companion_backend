package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.ShopNews;

import java.util.List;

@Getter
@Builder
public class ShopNewsResponse {
    private String title;
    private String content;
    private String nickname;
    private String shopId;
    private String shopName;
    private List<String> imgUrls;

    public static ShopNewsResponse toDto(ShopNews shopNews, List<String> imgUrls) {
        return ShopNewsResponse.builder()
                .title(shopNews.getTitle())
                .content(shopNews.getContent())
                .nickname(shopNews.getMember().getNickname())
                .shopId(shopNews.getShopId())
                .shopName(shopNews.getShop().getName())
                .imgUrls(imgUrls)
                .build();
    }
}