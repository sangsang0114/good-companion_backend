package org.example.dto.request;

import org.example.domain.Member;
import org.example.domain.RegionMark;

public record RegionMarkRequest(String regionId, Boolean isAdd) {
    public RegionMark toEntity(Member member) {
        return RegionMark.builder()
                .regionId(regionId)
                .memberId(member.getId())
                .build();
    }
}
