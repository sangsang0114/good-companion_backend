package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.Review;
import org.sku.zero.util.DatetimeUtil;

@Getter
@Builder
public class MyReviewHistoryResponse {
    private Long id;
    private String shopId;
    private String shopName;
    private String shopAddress;
    private Long memberId;
    private String memberNickname;
    private String comment;
    private Double score;
    private String createdAt;

    public static MyReviewHistoryResponse toDto(Review review) {
        return MyReviewHistoryResponse.builder()
                .id(review.getId())
                .shopId(review.getShopId())
                .shopName(review.getShop().getName())
                .shopAddress(review.getShop().getAddress())
                .memberId(review.getMemberId())
                .memberNickname(review.getMember().getNickname())
                .comment(review.getComment())
                .score(review.getScore())
                .createdAt(DatetimeUtil.formatDateTime(review.getCreatedAt()))
                .build();
    }
}
