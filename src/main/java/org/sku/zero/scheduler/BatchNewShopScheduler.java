package org.sku.zero.scheduler;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.sku.zero.application.MailService;
import org.sku.zero.application.redis.NewShopService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchNewShopScheduler {
    private final MailService mailService;
    private final NewShopService newShopService;

    @Scheduled(cron = "0 0 9 * * *")
    public void sendMailTest() throws MessagingException {
        newShopService.sendNewsShopInfoMail();
    }
}
