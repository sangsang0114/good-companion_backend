package org.sku.zero.dto.request;

import org.sku.zero.domain.Notice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record AddNoticeRequest(String title, String content, List<MultipartFile> files, Boolean isImportant) {
    public Notice toEntity(Long memberId) {
        return Notice.builder()
                .title(title)
                .memberId(memberId)
                .content(content)
                .build();
    }
}