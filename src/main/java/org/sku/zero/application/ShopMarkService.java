package org.sku.zero.application;

import lombok.RequiredArgsConstructor;
import org.sku.zero.domain.Member;
import org.sku.zero.domain.Shop;
import org.sku.zero.domain.ShopMark;
import org.sku.zero.dto.request.ShopMarkRequest;
import org.sku.zero.dto.response.ShopMarkResponse;
import org.sku.zero.infrastructure.repository.ShopMarkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopMarkService {
    private final ShopMarkRepository shopMarkRepository;
    private final MemberService memberService;
    private final ShopService shopService;

    public Boolean isExist(Principal principal, String shopId) {
        Member member = memberService.findByEmail(principal.getName());
        Shop shop = shopService.getShopById(shopId);
        ShopMark shopMark = shopMarkRepository.findShopMarkByMemberAndShop(member, shop).orElse(null);
        return shopMark != null;
    }

    @Transactional
    public Long addShopMark(ShopMarkRequest shopMarkRequest, Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        ShopMark shopMark = shopMarkRepository.save(shopMarkRequest.toEntity(member));
        return shopMark.getId();
    }

    public List<ShopMarkResponse> getShopMarksByMember(Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        List<ShopMark> shopMarks = shopMarkRepository.findShopMarksByMember(member);
        return shopMarks.stream().map(ShopMarkResponse::toDto).toList();
    }

    @Transactional
    public Boolean deleteShopMark(ShopMarkRequest shopMarkRequest, Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        Shop shop = shopService.getShopById(shopMarkRequest.shopId());
        ShopMark shopMark = shopMarkRepository.findShopMarkByMemberAndShop(member, shop).orElse(null);
        if (shopMark == null) {
            return false;
        } else {
            shopMarkRepository.delete(shopMark);
            return true;
        }
    }
}
