package org.example.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.domain.Member;
import org.example.domain.Notice;
import org.example.util.DatetimeUtil;

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
