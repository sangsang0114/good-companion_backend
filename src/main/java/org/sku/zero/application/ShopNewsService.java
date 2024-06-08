package org.sku.zero.application;

import lombok.RequiredArgsConstructor;
import org.sku.zero.domain.Member;
import org.sku.zero.domain.Shop;
import org.sku.zero.domain.ShopNews;
import org.sku.zero.dto.request.AddShopNewsRequest;
import org.sku.zero.dto.response.ShopMarkResponse;
import org.sku.zero.infrastructure.repository.ShopNewsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopNewsService {
    private final ShopNewsRepository shopNewsRepository;
    private final ShopService shopService;
    private final MemberService memberService;
    private final ShopMarkService shopMarkService;

    public Page<ShopNews> findNewsByShop(String shopId, Integer size, Integer page) {
        Pageable pageable = PageRequest.of(page, size);
        Shop shop = shopService.getShopById(shopId);
        return shopNewsRepository.findShopNewsByShop(shop, pageable);
    }

    public Page<ShopNews> findNewsByMarkedShops(Integer size, Integer page, Principal principal) {
        List<String> shopIds = shopMarkService.getShopMarksByMember(principal)
                .stream().map(ShopMarkResponse::getShopId).toList();
        Pageable pageable = PageRequest.of(page, size);
        return shopNewsRepository.findShopNewsByMarkedShops(shopIds, pageable);
    }

    @Transactional
    public Long addShopNews(AddShopNewsRequest addShopNewsRequest, Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        ShopNews saved = shopNewsRepository.save(addShopNewsRequest.toEntity(member));
        return saved.getId();
    }
}
