package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.Member;
import org.sku.zero.domain.RegistrationProposal;

@Getter
@Builder
public class ProposalDetailResponse {
    private Long id;
    private String memberNickname;
    private String memberEmail;
    private String shopName;
    private String shopAddress;
    private String shopPhone;
    private String businessHours;
    private String info;
    private String reason;
    private String sectorId;
    private String zipcode;
    private String status;

    public static ProposalDetailResponse toDto(RegistrationProposal proposal, Member member) {
        return ProposalDetailResponse.builder()
                .id(proposal.getId())
                .build();
    }
}
