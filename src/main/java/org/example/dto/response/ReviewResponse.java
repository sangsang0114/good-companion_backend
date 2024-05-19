package org.example.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReviewResponse {
    private Long id;
    private String shopId;
    private String writer;
    private String comment;
    private Double score;
    private List<Long> attachmentIndices;
    private LocalDateTime createdAt;
}
