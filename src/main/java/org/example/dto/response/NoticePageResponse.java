package org.example.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.domain.Notice;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class NoticePageResponse {
    private List<ListNoticeResponse> notices;
    private int totalPages;
    private int currentPage;
    private boolean hasPrevious;
    private boolean isFirstPage;
    private boolean isLastPage;
    private long totalElements;

    public static NoticePageResponse toDto(List<ListNoticeResponse> notices, Page<Notice> noticePage) {
        return NoticePageResponse.builder()
                .notices(notices)
                .totalPages(noticePage.getTotalPages())
                .currentPage(noticePage.getNumber())
                .hasPrevious(noticePage.hasPrevious())
                .isFirstPage(noticePage.isFirst())
                .isLastPage(noticePage.isLast())
                .totalElements(noticePage.getTotalElements())
                .build();
    }
}
