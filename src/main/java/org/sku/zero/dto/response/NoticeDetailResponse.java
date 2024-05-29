package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.Member;
import org.sku.zero.domain.Notice;
import org.sku.zero.util.DatetimeUtil;

import java.util.List;

@Getter
@Builder
public class NoticeDetailResponse {
    private List<String> imgUrls;
    private String title;
    private String content;
    private String author;
    private Integer viewCount;
    private String createdAt;

    public static NoticeDetailResponse toDto(Member member, Notice notice, List<String> imgUrls) {
        return NoticeDetailResponse.builder()
                .imgUrls(imgUrls)
                .title(notice.getTitle())
                .content(notice.getContent())
                .author(member.getNickname())
                .viewCount(notice.getViewCount())
                .createdAt(DatetimeUtil.formatDate(notice.getCreatedAt()))
                .build();
    }
}
