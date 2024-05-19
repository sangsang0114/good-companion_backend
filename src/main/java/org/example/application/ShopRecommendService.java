package org.example.application;

import lombok.RequiredArgsConstructor;
import org.example.domain.Member;
import org.example.domain.Shop;
import org.example.domain.ShopRecommend;
import org.example.dto.response.ListShopRecommendResponse;
import org.example.infrastructure.repository.ShopRecommendRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ShopRecommendService {
    private final ShopRecommendRepository shopRecommendRepository;
    private final MemberService memberService;
    private final ShopService shopService;

    public List<ListShopRecommendResponse> listShopRecommendResponses(Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        return shopRecommendRepository.findShopRecommendsByMemberId(member.getId());
    }

    private Member getMember(Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        return member;
    }

    @Transactional(readOnly = true)
    public Boolean isExist(Principal principal, String shopId) {
        Member member = getMember(principal);
        ShopRecommend shopRecommend = shopRecommendRepository.findByMemberIdAndShopId(member.getId(), shopId)
                .orElse(null);
        return shopRecommend != null;
    }

    @Transactional
    public Long addShopRecommend(Principal principal, String shopId) {
        Member member = getMember(principal);
        Shop shop = shopService.getShopById(shopId);
        if (isExist(principal, shopId)) return null;

        ShopRecommend shopRecommend = ShopRecommend.builder()
                .memberId(member.getId())
                .shopId(shop.getId())
                .build();
        return shopRecommendRepository.save(shopRecommend).getId();
    }

    @Transactional
    public Boolean deleteShopRecommend(Principal principal, String shopId) {
        Member member = memberService.findByEmail(principal.getName());
        Shop shop = shopService.getShopById(shopId);
        if (!isExist(principal, shopId)) return false;

        ShopRecommend shopRecommend = shopRecommendRepository.findByMemberIdAndShopId(member.getId(), shopId).get();
        shopRecommendRepository.delete(shopRecommend);
        return true;
    }
}
