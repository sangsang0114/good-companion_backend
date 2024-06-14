package org.sku.zero.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sku.zero.application.AttachmentService;
import org.sku.zero.application.FcmService;
import org.sku.zero.application.MailService;
import org.sku.zero.application.RegionMarkService;
import org.sku.zero.application.redis.NewShopService;
import org.sku.zero.domain.redis.NewShop;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewShopAddedListener implements ApplicationListener<NewShopAddedEvent> {
    private final NewShopService newShopService;

    @Override
    public void onApplicationEvent(NewShopAddedEvent event) {
        NewShop newShop = NewShop.builder()
                .shopId(event.getShopId())
                .name(event.getDto().name())
                .address(event.getDto().address())
                .phone(event.getDto().phone())
                .imgUrl(event.getFirstImgUrl())
                .build();
        newShopService.saveNewShop(newShop);
    }
}
