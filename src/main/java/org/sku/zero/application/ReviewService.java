package org.sku.zero.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.sku.zero.domain.Member;
import org.sku.zero.domain.Review;
import org.sku.zero.domain.Shop;
import org.sku.zero.dto.request.AddReviewRequest;
import org.sku.zero.dto.request.ModifyReviewRequest;
import org.sku.zero.dto.response.MyReviewHistoryResponse;
import org.sku.zero.dto.response.ReviewResponse;
import org.sku.zero.exception.ErrorCode;
import org.sku.zero.exception.NotFoundException;
import org.sku.zero.infrastructure.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
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
    @Value("${server.url}")
    private String serverUrl;

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
        return getReviewResponses(reviews);
    }

    public List<ReviewResponse> getSummaryReviewsByShopId(String shopId) {
        Shop shop = shopService.getShopById(shopId);
        List<Review> reviews = reviewRepository.findTop3ByShopOrderByIdDesc(shop);
        return getReviewResponses(reviews);
    }

    public List<ReviewResponse> getReviewByShopIdAndMember(String shopId, Principal principal) {
        Shop shop = shopService.getShopById(shopId);
        Member member = memberService.findByEmail(principal.getName());
        List<Review> reviews = reviewRepository.findReviewsByShopAndMemberOrderByIdDesc(shop, member);
        return getReviewResponses(reviews);
    }

    @NotNull
    private List<ReviewResponse> getReviewResponses(List<Review> reviews) {
        List<ReviewResponse> response = reviews.stream()
                .map(review -> {
                            List<Long> indices = attachmentService.getFileIndicesByServiceNameAndTarget("Review", review.getId());
                            List<String> imgUrls = indices.stream().map(index -> serverUrl + "/api/v1/attachment/" + index).toList();
                    return ReviewResponse.toDto(review, imgUrls);
                        }
                ).toList();
        return response;
    }

    public ReviewResponse getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REVIEW_NOT_FOUND));
        List<String> imgUrls = attachmentService.getFileIndicesByServiceNameAndTarget("Review", id)
                .stream().map(index -> serverUrl + "/api/v1/attachment/" + index).toList();
        return ReviewResponse.toDto(review, imgUrls);
    }

    @Transactional
    public Long modifyReview(ModifyReviewRequest requestDto, Principal principal) {
        Long reviewId = requestDto.reviewId();
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new NotFoundException(ErrorCode.REVIEW_NOT_FOUND));
        review.editReview(requestDto.comment(), requestDto.score());

        if (requestDto.deletedFiles() != null && !requestDto.deletedFiles().isEmpty()) {
            List<Long> attachmentIds = requestDto.deletedFiles().stream()
                    .map((deletedFile -> Long.parseLong(deletedFile.replaceAll(serverUrl + "/api/v1/attachment/", ""))
                    )).toList();

            for (Long attachmentId : attachmentIds) {
                try {
                    attachmentService.removeAttachmentById(attachmentId);
                } catch (Exception e) {
                    System.out.println("test");
                }
            }
        }

        if (requestDto.newFiles() != null && !requestDto.newFiles().isEmpty()) {
            attachmentService.uploadFile(requestDto.newFiles(), "Review", requestDto.reviewId());
        }
        return reviewId;
    }

    @Transactional
    public Boolean deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(()-> new NotFoundException(ErrorCode.REVIEW_NOT_FOUND));
        reviewRepository.delete(review);
        return true;
    }

    public List<MyReviewHistoryResponse> getReviewBYMember(Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        List<Review> reviews = reviewRepository.findReviewsByMemberOrderByIdDesc(member);
        return reviews.stream().map(MyReviewHistoryResponse::toDto).toList();
    }
}
