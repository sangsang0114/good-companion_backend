package org.sku.zero.application;

import lombok.RequiredArgsConstructor;
import org.sku.zero.domain.Member;
import org.sku.zero.domain.Shop;
import org.sku.zero.domain.ShopManager;
import org.sku.zero.dto.response.ShopManagerResponse;
import org.sku.zero.infrastructure.repository.ShopManagerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ShopManagerService {
    private final ShopManagerRepository shopManagerRepository;
    private final ShopService shopService;
    private final MemberService memberService;

    @Transactional
    public void saveShopManager(String shopId, Long memberId) {
        Shop shop = shopService.getShopById(shopId);
        Member member = memberService.findById(memberId);

    }

    @Transactional
    public List<ShopManagerResponse> findByMember(Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        List<ShopManager> shopManagers = shopManagerRepository.findByMember(member);
        List<ShopManagerResponse> shopManagerResponses =
                shopManagers.stream().map(shopManager -> ShopManagerResponse.toDto(shopManager.getShop()))
                        .toList();
        return shopManagerResponses;
    }

    public Boolean existsByShopAndMember(String shopId, Principal principal) {
        Shop shop = shopService.getShopById(shopId);
        Member member = memberService.findByEmail(principal.getName());
        return shopManagerRepository.existsByShopAndMember(shop, member);
    }
}
