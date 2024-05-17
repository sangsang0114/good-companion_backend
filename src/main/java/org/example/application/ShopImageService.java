package org.example.application;

import lombok.RequiredArgsConstructor;
import org.example.domain.ShopImage;
import org.example.infrastructure.repository.ShopImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShopImageService {
    private final ShopImageRepository shopImageRepository;

    @Transactional
    public ShopImage save(ShopImage shopImage) {
        return shopImageRepository.save(shopImage);
    }

    @Transactional(readOnly = true)
    public String getShopImageUrlByShopId(String shopId) {
        ShopImage shopImage = shopImageRepository.findByShopId(shopId)
                .orElse(null);
        return shopImage == null ? null : shopImage.getUrl();
    }
}
