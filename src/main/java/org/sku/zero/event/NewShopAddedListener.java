package org.sku.zero.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sku.zero.application.FcmService;
import org.sku.zero.application.MailService;
import org.sku.zero.application.RegionMarkService;
import org.sku.zero.application.redis.NewShopService;
import org.sku.zero.domain.redis.NewShop;
import org.sku.zero.dto.external.FcmSendRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewShopAddedListener implements ApplicationListener<NewShopAddedEvent> {
    private final NewShopService newShopService;
    private final MailService mailService;
    private final FcmService fcmService;
    private final RegionMarkService regionMarkService;

    @Value("${web.url}")
    private String webUrl;

    @Override
    public void onApplicationEvent(NewShopAddedEvent event) {
        NewShop newShop = NewShop.builder()
                .shopId(event.getShop().getId())
                .name(event.getShop().getName())
                .address(event.getShop().getAddress())
                .phone(event.getShop().getPhone())
                .imgUrl(event.getFirstImageUrl())
                .build();
        newShopService.saveNewShop(newShop);

        List<String> fcmTokens = regionMarkService.getFcmTokensByRegionMark(event.getShop().getShopRegion());

        for (String token : fcmTokens) {
            FcmSendRequest fcmRequest = FcmSendRequest.builder()
                    .token(token)
                    .title("새로운 가게가 추가되었어요")
                    .body(event.getShop().getName() + "\n" + event.getShop().getAddress())
                    .imageUrl(event.getFirstImageUrl())
                    .clickAction(webUrl + "/detail/" + event.getShop().getId())
                    .build();
            try {
                fcmService.sendMessageTo(fcmRequest);
            } catch (IOException e) {
                log.error("Failed to send FCM message", e);
            }
        }
    }
}
