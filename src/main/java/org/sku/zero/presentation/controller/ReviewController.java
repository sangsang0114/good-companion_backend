package org.sku.zero.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.sku.zero.application.ReviewService;
import org.sku.zero.dto.request.AddReviewRequest;
import org.sku.zero.dto.response.ReviewResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/")
    public ResponseEntity<Long> addShopReview(@ModelAttribute AddReviewRequest addReviewRequest, Authentication authentication) {
        Long id = reviewService.addReview(addReviewRequest, authentication);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(id);
    }

    @GetMapping("/")
    public ResponseEntity<List<ReviewResponse>> findReviewsByShopId(@RequestParam String shopId) {
        List<ReviewResponse> responses = reviewService.getReviewByShopId(shopId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responses);
    }

    @GetMapping("/summary")
    public ResponseEntity<List<ReviewResponse>> findSummaryReviewByShopId(@RequestParam String shopId) {
        List<ReviewResponse> responses = reviewService.getSummaryReviewsByShopId(shopId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responses);
    }
}
