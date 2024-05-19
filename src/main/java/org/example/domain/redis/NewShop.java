package org.example.domain.redis;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "new_shop", timeToLive = 3600)
public class NewShop {

    @Id
    private String shopId;

    private String name;
    private String address;
    private String phone;
    private List<Long> attachmentIds;

    @Builder
    public NewShop(String shopId, String name, String address, String phone, List<Long> attachmentIds) {
        this.shopId = shopId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.attachmentIds = attachmentIds;
    }
}
