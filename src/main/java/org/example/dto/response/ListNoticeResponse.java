package org.example.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.domain.Notice;

import java.time.LocalDateTime;

@Getter
@Builder
public class ListNoticeResponse {
    private String title;
    private Integer viewCount;
    private LocalDateTime createdAt;

    public static ListNoticeResponse toDto(Notice notice) {
        return ListNoticeResponse.builder()
                .title(notice.getTitle())
                .viewCount(notice.getViewCount())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}
