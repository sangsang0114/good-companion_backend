package org.sku.zero.dto.request;

import org.sku.zero.domain.Member;
import org.sku.zero.domain.RegionMark;

public record RegionMarkRequest(String regionId, Boolean isAdd) {
    public RegionMark toEntity(Member member) {
        return RegionMark.builder()
                .regionId(regionId)
                .memberId(member.getId())
                .build();
    }
}
