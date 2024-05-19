package org.example.application;

import lombok.RequiredArgsConstructor;
import org.example.domain.Member;
import org.example.domain.RegionMark;
import org.example.infrastructure.repository.RegionMarkRepository;
import org.example.dto.request.AddRegionMarkRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RegionMarkService {
    private final RegionMarkRepository regionMarkRepository;
    private final MemberService memberService;


    public Long addRegionMark(AddRegionMarkRequest regionMarkRequest, Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        RegionMark regionMark = regionMarkRepository.save(
                regionMarkRequest.toEntity(member)
        );
        return regionMark.getId();
    }

    public List<String> getEmailsByRegionId(String regionId) {
        List<String> emails = regionMarkRepository.findEmailsByRegionId(regionId);
        return emails;
    }

    public Boolean removeRegionMark(String regionId, Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        RegionMark regionMark = regionMarkRepository.findRegionMarkByMemberIdAndRegionId(member.getId(), regionId)
                .orElseThrow(() -> new RuntimeException("Region mark not found"));
        regionMarkRepository.delete(regionMark);
        return true;
    }
}
