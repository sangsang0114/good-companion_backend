package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.Member;
import org.sku.zero.domain.RegistrationProposal;

import java.time.LocalDateTime;

@Getter
@Builder
public class ListProposalResponse {
    private Long id;
    private String shopName;
    private String memberNickname;
    private String status;
    private LocalDateTime createdAt;


    public static ListProposalResponse toDto(RegistrationProposal proposal, Member member) {
        return ListProposalResponse.builder()
                .id(proposal.getId())
                .shopName(proposal.getShopName())
                .memberNickname(member.getNickname())
                .status(proposal.getStatus().name())
                .createdAt(proposal.getCreatedAt())
                .build();
    }
}
