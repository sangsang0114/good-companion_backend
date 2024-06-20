package org.sku.zero.domain.redis;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "new_shop", timeToLive = 86400)
public class NewShop {

    @Id
    private String shopId;

    private String name;
    private String address;
    private String phone;
    private String imgUrl;

    @Builder
    public NewShop(String shopId, String name, String address, String phone, String imgUrl) {
        this.shopId = shopId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.imgUrl = imgUrl;
    }
}
