package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.Review;
import org.sku.zero.util.DatetimeUtil;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReviewResponse {
    private Long id;
    private String shopId;
    private Long memberId;
    private String memberNickname;
    private String comment;
    private Double score;
    private List<String> imgUrls;
    private String createdAt;

    public static ReviewResponse toDto(Review review, List<String> imgUrls) {
        return ReviewResponse.builder()
                .id(review.getId())
                .shopId(review.getShopId())
                .memberId(review.getMemberId())
                .memberNickname(review.getMember().getNickname())
                .comment(review.getComment())
                .score(review.getScore())
                .createdAt(DatetimeUtil.formatDateTime(review.getCreatedAt()))
                .imgUrls(imgUrls)
                .build();
    }
}
