package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.ShopNews;

@Getter
@Builder
public class ShopNewsResponse {
    private String title;
    private String content;
    private String nickname;

    public static ShopNewsResponse toDto(ShopNews shopNews) {
        return ShopNewsResponse.builder()
                .title(shopNews.getTitle())
                .content(shopNews.getContent())
                .nickname(shopNews.getMember().getNickname())
                .build();
    }
}