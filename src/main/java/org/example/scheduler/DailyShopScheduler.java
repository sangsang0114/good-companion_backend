package org.example.scheduler;

import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.example.application.ShopService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyShopScheduler {
    private final ShopService shopService;

    @Scheduled(cron = "0 0 6 * * *")
    public void fetchDailyShop(){
        shopService.fetchDailyShop();
    }
}
