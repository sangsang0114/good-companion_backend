package org.sku.zero.dto.request;

import lombok.Builder;
import org.sku.zero.domain.Member;
import org.sku.zero.domain.ShopMark;

@Builder
public record ShopMarkRequest(String shopId) {
    public ShopMark toEntity(Member member) {
        return ShopMark.builder()
                .shopId(shopId)
                .memberId(member.getId())
                .build();
    }
}