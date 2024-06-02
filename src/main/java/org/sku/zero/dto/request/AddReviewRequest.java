package org.sku.zero.dto.request;

import org.sku.zero.domain.Review;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record AddReviewRequest(String shopId, Double score, String comment, List<MultipartFile> files) {
    public Review toEntity(Long memberId) {
        return Review.builder()
                .shopId(shopId)
                .memberId(memberId)
                .score(score)
                .comment(comment)
                .build();
    }
}