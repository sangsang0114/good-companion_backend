package org.sku.zero.application;

import lombok.RequiredArgsConstructor;
import org.sku.zero.domain.Member;
import org.sku.zero.domain.Shop;
import org.sku.zero.domain.ShopNews;
import org.sku.zero.dto.request.AddShopNewsRequest;
import org.sku.zero.dto.response.ShopMarkResponse;
import org.sku.zero.dto.response.ShopNewsPageResponse;
import org.sku.zero.dto.response.ShopNewsResponse;
import org.sku.zero.infrastructure.repository.ShopNewsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopNewsService {
    private final ShopNewsRepository shopNewsRepository;
    private final ShopService shopService;
    private final MemberService memberService;
    private final ShopMarkService shopMarkService;
    private final AttachmentService attachmentService;

    @Value("${server.url}")
    private String serverUrl;

    public ShopNewsPageResponse findNewsByShop(String shopId, Integer size, Integer page) {
        Pageable pageable = PageRequest.of(page, size);
        Shop shop = shopService.getShopById(shopId);
        Page<ShopNews> shopNewsPage = shopNewsRepository.findShopNewsByShop(shop, pageable);

        return getShopNewsPageResponse(shopNewsPage);
    }

    public ShopNewsPageResponse findNewsByMarkedShops(Integer size, Integer page, Principal principal) {
        List<String> shopIds = shopMarkService.getShopMarksByMember(principal)
                .stream().map(ShopMarkResponse::getShopId).toList();
        Pageable pageable = PageRequest.of(page, size);
        Page<ShopNews> shopNewsPage = shopNewsRepository.findShopNewsByMarkedShops(shopIds, pageable);

        return getShopNewsPageResponse(shopNewsPage);
    }

    private ShopNewsPageResponse getShopNewsPageResponse(Page<ShopNews> shopNewsPage) {
        List<ShopNewsResponse> shopNewsResponses = shopNewsPage.stream().map(shopNews -> {
            List<Long> attachmentIds = attachmentService.getFileIndicesByServiceNameAndTarget("ShopNews", shopNews.getId());
            List<String> imgUrls = new ArrayList<>();
            if (!attachmentIds.isEmpty()) {
                imgUrls = attachmentIds.stream().map(id -> serverUrl + "/api/v1/attachment/" + id).toList();
            }
            return ShopNewsResponse.toDto(shopNews, imgUrls);
        }).toList();
        return ShopNewsPageResponse.toDto(shopNewsResponses, shopNewsPage);
    }

    @Transactional
    public Long addShopNews(AddShopNewsRequest addShopNewsRequest, Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        ShopNews saved = shopNewsRepository.save(addShopNewsRequest.toEntity(member));
        List<MultipartFile> files = addShopNewsRequest.files();
        attachmentService.uploadFile(files, "ShopNews", saved.getId());

        return saved.getId();
    }
}
