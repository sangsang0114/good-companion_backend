package org.example.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record ModifyProductRequest(Long id, Boolean isDeleteImage, Long attachmentId, Integer price, MultipartFile file) {
}
