package org.sku.zero.dto.request;

import org.sku.zero.domain.Member;
import org.sku.zero.domain.RegistrationProposal;

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
