package org.sku.zero.application;

import lombok.RequiredArgsConstructor;
import org.sku.zero.domain.Member;
import org.sku.zero.domain.Shop;
import org.sku.zero.domain.ShopManager;
import org.sku.zero.dto.request.ShopManagerRequest;
import org.sku.zero.dto.response.ShopManagerResponse;
import org.sku.zero.exception.BadRequestException;
import org.sku.zero.exception.ErrorCode;
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
    public Long addShopManager(ShopManagerRequest shopManagerRequest) {
        Shop shop = shopService.getShopById(shopManagerRequest.shopId());
        Member member = memberService.findById(shopManagerRequest.memberId());
        ShopManager saved = shopManagerRepository.save(shopManagerRequest.toEntity(shop, member));
        return saved.getId();
    }

    @Transactional
    public Boolean removeShopManager(Long id) {
        ShopManager shopManager = shopManagerRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_ARGUMENTS));
        shopManagerRepository.delete(shopManager);
        return true;
    }

    @Transactional
    public List<ShopManagerResponse> findByMember(Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        List<ShopManager> shopManagers = shopManagerRepository.findByMember(member);
        List<ShopManagerResponse> shopManagerResponses =
                shopManagers.stream().map(ShopManagerResponse::toDto)
                        .toList();
        return shopManagerResponses;
    }

    public Boolean existsByShopAndMember(String shopId, Principal principal) {
        Shop shop = shopService.getShopById(shopId);
        Member member = memberService.findByEmail(principal.getName());
        return shopManagerRepository.existsByShopAndMember(shop, member);
    }

    public List<ShopManagerResponse> findByShopOrMember(String shopId, Long memberId) {
        List<ShopManager> shopManagers = null;
        if ((shopId == null && memberId == null) || (shopId != null && memberId != null)) {
            throw new BadRequestException(ErrorCode.INVALID_ARGUMENTS);
        } else if (shopId != null) {
            Shop shop = shopService.getShopById(shopId);
            shopManagers = shopManagerRepository.findByShop(shop);
        } else {
            Member member = memberService.findById(memberId);
            shopManagers = shopManagerRepository.findByMember(member);
        }
        return shopManagers.stream().map(ShopManagerResponse::toDto).toList();
    }

}
