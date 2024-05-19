package org.example.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.domain.Member;
import org.example.domain.RegistrationProposal;

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
