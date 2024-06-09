package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;

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
    private LocalDateTime createdAt;
}
