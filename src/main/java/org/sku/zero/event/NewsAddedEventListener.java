package org.sku.zero.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sku.zero.application.FcmService;
import org.sku.zero.application.ShopMarkService;
import org.sku.zero.dto.external.FcmSendRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewsAddedEventListener implements ApplicationListener<NewsAddedEvent> {

    private final static String FCM_PUSH_TITLE = "즐겨찾기 한 가게에 새 소식이 떴어요";
    private final ShopMarkService shopMarkService;
    private final FcmService fcmService;
    @Value("${web.url}")
    private String webUrl;

    @Override
    public void onApplicationEvent(NewsAddedEvent event) {
        List<String> fcmTokens = shopMarkService.getFcmTokensByShopMark(event.getShopNews().getShopId());

        for (String token: fcmTokens){
            FcmSendRequest fcmSendRequest = FcmSendRequest.builder()
                    .token(token)
                    .title(FCM_PUSH_TITLE)
                    .body(String.format("%s by %s", event.getShopNews().getTitle(), event.getShop().getName()))
                    .url(webUrl)
                    .build();
            try {
                fcmService.sendMessageTo(fcmSendRequest);
            } catch (IOException e) {
                log.error("Failed to send FCM message", e);
            }
        }
    }
}
