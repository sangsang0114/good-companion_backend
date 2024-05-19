package org.example.event;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.FcmService;
import org.example.application.MailService;
import org.example.application.RegionMarkService;
import org.example.application.redis.NewShopService;
import org.example.domain.redis.NewShop;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewShopAddedListener implements ApplicationListener<NewShopAddedEvent> {
    private final MailService mailService;
    private final FcmService fcmService;
    private final RegionMarkService regionMarkService;
    private final NewShopService newShopService;

    @Override
    public void onApplicationEvent(NewShopAddedEvent event) {
//        try {
//            mailService.sendMail("envy15@skuniv.ac.kr", event.getDto());
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//        List<String> emails = regionMarkService.getEmailsByRegionId(event.getDto().getRegionId());
//        for (String email : emails) {
//            try {
//                mailService.sendMail(email, event.getDto());
//            } catch (MessagingException e) {
//                throw new RuntimeException(e);
//            }
//        }

//

        NewShop newShop = NewShop.builder()
                .shopId(event.getDto().id())
                .name(event.getDto().name())
                .address(event.getDto().address())
                .phone(event.getDto().phone())
                .build();
        newShopService.saveNewShop(newShop);
    }
}
