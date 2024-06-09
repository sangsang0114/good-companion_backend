package org.sku.zero.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sku.zero.domain.Member;
import org.sku.zero.domain.Review;
import org.sku.zero.domain.Shop;
import org.sku.zero.dto.request.AddReviewRequest;
import org.sku.zero.dto.response.ReviewResponse;
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
        List<Review> reviews = reviewRepository.findTop3ByShopOrderByIdDesc(shop);
        List<ReviewResponse> response = reviews.stream()
                .map(review -> {
                            List<Long> indices = attachmentService.getFileIndicesByServiceNameAndTarget("Review", review.getId());
                            List<String> imgUrls = indices.stream().map(index -> serverUrl + "/api/v1/attachment/" + index).toList();
                            return ReviewResponse.builder()
                                    .id(review.getId())
                                    .score(review.getScore())
                                    .memberNickname(review.getMember().getNickname())
                                    .shopId(review.getShopId())
                                    .comment(review.getComment())
                                    .imgUrls(imgUrls)
                                    .createdAt(review.getCreatedAt())
                                    .build();
                        }
                ).toList();
        return response;
    }

    public List<ReviewResponse> getSummaryReviewsByShopId(String shopId) {
        Shop shop = shopService.getShopById(shopId);
        List<Review> reviews = reviewRepository.findTop3ByShopOrderByIdDesc(shop);
        List<ReviewResponse> response = reviews.stream()
                .map(review -> {
                            List<Long> indices = attachmentService.getFileIndicesByServiceNameAndTarget("Review", review.getId());
                            List<String> imgUrls = indices.stream().map(index -> serverUrl + "/api/v1/attachment/" + index).toList();
                            return ReviewResponse.builder()
                        .id(review.getId())
                        .score(review.getScore())
                                    .memberNickname(review.getMember().getNickname())
                        .shopId(review.getShopId())
                        .comment(review.getComment())
                                    .imgUrls(imgUrls)
                        .createdAt(review.getCreatedAt())
                                    .build();
                        }
                ).toList();
        return response;
    }

    public List<ReviewResponse> getReviewByShopIdAndMember(String shopId, Principal principal) {
        Shop shop = shopService.getShopById(shopId);
        Member member = memberService.findByEmail(principal.getName());
        List<Review> reviews = reviewRepository.findTop3ByShopOrderByIdDesc(shop);
        List<ReviewResponse> response = reviews.stream()
                .map(review -> {
                            List<Long> indices = attachmentService.getFileIndicesByServiceNameAndTarget("Review", review.getId());
                            List<String> imgUrls = indices.stream().map(index -> serverUrl + "/api/v1/attachment/" + index).toList();
                            return ReviewResponse.builder()
                                    .id(review.getId())
                                    .score(review.getScore())
                                    .memberNickname(review.getMember().getNickname())
                                    .shopId(review.getShopId())
                                    .comment(review.getComment())
                                    .imgUrls(imgUrls)
                                    .createdAt(review.getCreatedAt())
                                    .build();
                        }
                ).toList();
        return response;
    }
}
