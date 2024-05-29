package org.sku.zero.application;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sku.zero.domain.Member;
import org.sku.zero.domain.RegionMark;
import org.sku.zero.dto.request.RegionMarkRequest;
import org.sku.zero.dto.response.RegionNotificationTargetResponse;
import org.sku.zero.infrastructure.repository.RegionMarkRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RegionMarkService {
    private final RegionMarkRepository regionMarkRepository;
    private final MemberService memberService;

    public List<String> getRegionMarkIdsByMember(Principal principal) {
        String email = principal.getName();
        Member member = memberService.findByEmail(email);
        List<RegionMark> regionMarks = regionMarkRepository.findRegionMarksByMember(member);

        return regionMarks.stream().map(RegionMark::getRegionId).toList();
    }

    public void editRegionMark(RegionMarkRequest regionMarkRequest, Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        if (regionMarkRequest.isAdd()) {
            regionMarkRepository.save(regionMarkRequest.toEntity(member));
        } else {
            RegionMark regionMark = regionMarkRepository.findRegionMarkByMemberIdAndRegionId(member.getId(), regionMarkRequest.regionId())
                    .orElseThrow(() -> new EntityNotFoundException("Region mark not found"));
            regionMarkRepository.delete(regionMark);
        }
    }

    public List<String> getEmailsByRegionId(String regionId) {
        List<String> emails = regionMarkRepository.findEmailsByRegionId(regionId);
        return emails;
    }

    public RegionNotificationTargetResponse getFcmTokensAndEmailsByRegionId(String regionId) {
        List<String> tokens = regionMarkRepository.findFcmTokensByRegionId(regionId);
        List<String> emails = regionMarkRepository.findEmailsByRegionId(regionId);
        return RegionNotificationTargetResponse.builder()
                .fcmTokens(tokens)
                .emails(emails)
                .build();
    }
}
