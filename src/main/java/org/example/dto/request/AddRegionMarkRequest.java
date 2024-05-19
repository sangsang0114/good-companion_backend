package org.example.dto.request;

import org.example.domain.Member;
import org.example.domain.RegionMark;

public record AddRegionMarkRequest(String regionId) {
    public RegionMark toEntity(Member member) {
        return RegionMark.builder()
                .memberId(member.getId())
                .regionId(regionId)
                .build();
    }
}
