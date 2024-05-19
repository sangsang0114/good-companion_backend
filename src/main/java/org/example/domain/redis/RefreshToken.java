package org.example.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken")
public class RefreshToken {
    @Id
    private String memberId;

    @Indexed
    private String token;

    @TimeToLive
    private Long ttl;
}
