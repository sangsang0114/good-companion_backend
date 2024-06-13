package org.sku.zero.application;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sku.zero.domain.Member;
import org.sku.zero.domain.RegionMark;
import org.sku.zero.dto.request.RegionMarkRequest;
import org.sku.zero.infrastructure.repository.RegionMarkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RegionMarkService {
    private final RegionMarkRepository regionMarkRepository;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public List<String> getRegionMarkIdsByMember(Principal principal) {
        String email = principal.getName();
        Member member = memberService.findByEmail(email);
        List<RegionMark> regionMarks = regionMarkRepository.findRegionMarksByMember(member);

        return regionMarks.stream().map(RegionMark::getRegionId).toList();
    }

    @Transactional
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
}
