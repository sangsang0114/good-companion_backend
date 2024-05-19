package org.example.dto.request;

import org.example.domain.Member;
import org.example.domain.RegistrationProposal;

public record AddRegistrationProposalRequest(
        String shopName, String shopAddress, String shopPhone,
        String businessHours, String info, String reason, String sectorId,
        String zipcode
) {
    public RegistrationProposal toEntity(Member member) {
        return RegistrationProposal.builder()
                .shopName(shopName)
                .shopAddress(shopAddress)
                .shopPhone(shopPhone)
                .businessHours(businessHours)
                .info(info)
                .reason(reason)
                .sectorId(sectorId)
                .zipcode(zipcode)
                .memberId(member.getId())
                .build();
    }
}
