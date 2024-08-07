package org.sku.zero.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 86400)
public class RefreshToken {
    @Id
    private String memberId;

    @Indexed
    private String token;

    @TimeToLive
    private Long ttl;
}
