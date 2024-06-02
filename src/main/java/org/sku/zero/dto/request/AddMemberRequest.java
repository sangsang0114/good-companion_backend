package org.sku.zero.dto.request;

import org.sku.zero.domain.Member;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record AddMemberRequest(String email, String password, String nickname) {
    public Member toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .build();
    }
}