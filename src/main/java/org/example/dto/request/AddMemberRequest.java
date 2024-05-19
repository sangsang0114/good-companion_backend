package org.example.dto.request;

import org.example.domain.Member;

public record AddMemberRequest(String email, String password, String nickname) {
    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }
}