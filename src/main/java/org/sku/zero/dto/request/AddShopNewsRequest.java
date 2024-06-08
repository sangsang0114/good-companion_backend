package org.sku.zero.dto.request;

import org.sku.zero.domain.Member;
import org.sku.zero.domain.ShopNews;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record AddShopNewsRequest(String shopId, String title, String content, List<MultipartFile> files) {
    public ShopNews toEntity(Member member) {
        return ShopNews.builder()
                .memberId(member.getId())
                .title(title)
                .content(content)
                .shopId(shopId)
                .build();
    }
}