package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.Notice;
import org.sku.zero.util.DatetimeUtil;

@Getter
@Builder
public class ListNoticeResponse {
    private Long id;
    private String title;
    private Integer viewCount;
    private String createdAt;
    private String author;

    public static ListNoticeResponse toDto(Notice notice) {
        return ListNoticeResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .viewCount(notice.getViewCount())
                .createdAt(DatetimeUtil.formatDate(notice.getCreatedAt()))
                .author(notice.getMember().getNickname())
                .build();
    }
}
