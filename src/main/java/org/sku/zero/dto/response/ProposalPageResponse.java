package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.RegistrationProposal;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class ProposalPageResponse {
    private List<ListProposalResponse> proposals;
    private int totalPages;
    private int currentPage;
    private boolean hasPrevious;
    private boolean isFirstPage;
    private boolean isLastPage;
    private long totalElements;

    public static ProposalPageResponse toDto(List<ListProposalResponse> proposalList, Page<RegistrationProposal> proposalPage) {
        return ProposalPageResponse.builder()
                .proposals(proposalList)
                .totalPages(proposalPage.getTotalPages())
                .currentPage(proposalPage.getNumber())
                .hasPrevious(proposalPage.hasPrevious())
                .isFirstPage(proposalPage.isFirst())
                .isLastPage(proposalPage.isLast())
                .totalElements(proposalPage.getTotalElements())
                .build();
    }
}
