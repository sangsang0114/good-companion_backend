package org.sku.zero.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ModifyNoticeRequest(Long noticeId, String title, String content,
                                  List<MultipartFile> newFiles, List<String> deletedFiles) {
}
