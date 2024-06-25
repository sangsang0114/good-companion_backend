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
    private Long memberPk;
    private String shopName;
    private String shopAddress;
    private String shopPhone;
    private String businessHours;
    private String info;
    private String reason;
    private String sectorId;
    private String zipcode;
    private String status;
    private String memo;

    public static ProposalDetailResponse toDto(RegistrationProposal proposal, Member member) {
        return ProposalDetailResponse.builder()
                .id(proposal.getId())
                .memberNickname(member.getNickname())
                .memberEmail(member.getEmail())
                .memberPk(member.getId())
                .shopName(proposal.getShopName())
                .shopAddress(proposal.getShopAddress())
                .shopPhone(proposal.getShopPhone())
                .businessHours(proposal.getBusinessHours())
                .info(proposal.getInfo())
                .reason(proposal.getReason())
                .sectorId(proposal.getSectorId())
                .zipcode(proposal.getZipcode())
                .status(proposal.getStatus().name())
                .memo(proposal.getMemo())
                .build();
    }
}
