package org.example.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Member;
import org.example.domain.Review;
import org.example.domain.Shop;
import org.example.dto.request.AddReviewRequest;
import org.example.dto.response.ReviewResponse;
import org.example.infrastructure.repository.ReviewRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AttachmentService attachmentService;
    private final ShopService shopService;
    private final MemberService memberService;
    private final String SERVICE_NAME = "Review";

    @Transactional
    public Long addReview(AddReviewRequest requestDto, Authentication authentication) {
        Member member = memberService.findByEmail(authentication.getName());
        Shop shop = shopService.getShopById(requestDto.shopId());

        Review review = reviewRepository.save(requestDto.toEntity(member.getId()));

        List<MultipartFile> files = requestDto.files();
        attachmentService.uploadFile(files, SERVICE_NAME, review.getId());
        return review.getId();
    }

    public List<ReviewResponse> getReviewByShopId(String shopId) {
        Shop shop = shopService.getShopById(shopId);
        List<Review> reviews = reviewRepository.findReviewsByShop(shop);
        List<ReviewResponse> response = reviews.stream()
                .map(review -> ReviewResponse.builder()
                        .id(review.getId())
                        .score(review.getScore())
                        .writer(review.getMember().getNickname())
                        .shopId(review.getShopId())
                        .comment(review.getComment())
                        .attachmentIndices(attachmentService.getFileIndicesByServiceNameAndTarget("Review", review.getId()))
                        .createdAt(review.getCreatedAt())
                        .build()
                ).toList();
        return response;
    }

    public List<ReviewResponse> getSummaryReviewsByShopId(String shopId) {
        Shop shop = shopService.getShopById(shopId);
        List<Review> reviews = reviewRepository.findTop3ByShopOrderByIdDesc(shop);
        List<ReviewResponse> response = reviews.stream()
                .map(review -> ReviewResponse.builder()
                        .id(review.getId())
                        .score(review.getScore())
                        .writer(review.getMember().getNickname())
                        .shopId(review.getShopId())
                        .comment(review.getComment())
                        .attachmentIndices(attachmentService.getFileIndicesByServiceNameAndTarget("Review", review.getId()))
                        .createdAt(review.getCreatedAt())
                        .build()
                ).toList();
        return response;
    }
}
