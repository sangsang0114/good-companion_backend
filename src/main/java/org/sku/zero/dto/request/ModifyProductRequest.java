package org.sku.zero.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record ModifyProductRequest(Long id, Boolean isDeleteImage, String attachmentUrl, Integer price, MultipartFile file) {
}
