package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.Member;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class MemberPageResponse {
    private List<MemberResponse> memberList;
    private int totalPages;
    private int currentPage;
    private boolean hasPrevious;
    private boolean isFirstPage;
    private boolean isLastPage;
    private long totalElements;

    public static MemberPageResponse toDto(List<MemberResponse> memberList, Page<Member> memberPage) {
        return MemberPageResponse.builder()
                .memberList(memberList)
                .totalPages(memberPage.getTotalPages())
                .currentPage(memberPage.getNumber())
                .hasPrevious(memberPage.hasPrevious())
                .isFirstPage(memberPage.isFirst())
                .isLastPage(memberPage.isLast())
                .totalElements(memberPage.getSize())
                .build();
    }
}
