package org.sku.zero.application.redis;

import lombok.RequiredArgsConstructor;
import org.sku.zero.domain.redis.DailyShop;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DailyShopService {
    private final RedisTemplate<String, DailyShop> redisTemplate;

    public void saveDailyShop(DailyShop dailyShop) {
        redisTemplate.opsForValue().set("daily_shop:" + dailyShop.getShopId(), dailyShop);
    }

    public void deleteAllDailyShops(){
        Set<String> keys = redisTemplate.keys("daily_shop:*");
        if(keys!= null && !keys.isEmpty())
            redisTemplate.delete(keys);
    }

    public List<DailyShop> listDailyShop() {
        List<DailyShop> dailyShops = new ArrayList<>();
        ScanOptions scanOptions = ScanOptions.scanOptions().match("daily_shop:*").count(100).build();

        Cursor<String> cursor = redisTemplate.scan(scanOptions);
        while (cursor.hasNext()) {
            String key = cursor.next();
            DailyShop dailyShop = redisTemplate.opsForValue().get(key);
            dailyShops.add(dailyShop);
        }
        return dailyShops;
    }
}
