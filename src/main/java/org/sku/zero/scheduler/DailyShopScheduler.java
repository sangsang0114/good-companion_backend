package org.sku.zero.scheduler;

import lombok.RequiredArgsConstructor;
import org.sku.zero.application.ShopService;
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
