package org.example.application.redis;

import lombok.RequiredArgsConstructor;
import org.example.application.AttachmentService;
import org.example.domain.redis.NewShop;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewShopService {
    private final RedisTemplate<String, NewShop> redisTemplate;

    public void saveNewShop(NewShop newShop) {
        redisTemplate.opsForValue().set("new_shop:"+newShop.getShopId(), newShop);
    }

    public List<NewShop> listNewShops() {
        List<NewShop> newshops = new ArrayList<>();
        ScanOptions scanOptions = ScanOptions.scanOptions().match("new_shop:*").count(100).build();

        Cursor<String> cursor = redisTemplate.scan(scanOptions);
        while (cursor.hasNext()) {
            String key = cursor.next();
            NewShop newShop = redisTemplate.opsForValue().get(key);
            newshops.add(newShop);
        }
        return newshops;
    }
}
