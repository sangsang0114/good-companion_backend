package org.sku.zero.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ModifyReviewRequest(Long reviewId, String comment, Double score,
                                  List<MultipartFile> newFiles, List<String> deletedFiles) {
}
