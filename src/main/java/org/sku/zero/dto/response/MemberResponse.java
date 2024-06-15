package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.Member;
import org.sku.zero.util.DatetimeUtil;

@Getter
@Builder
public class MemberResponse {
    private Long id;
    private String email;
    private String nickname;
    private String role;
    private String joinedAt;
    private String updatedAt;
    private Integer isAvailable;
    private Integer emailFlag;
    private String fcmToken;
    private Integer fcmFlag;

    public static MemberResponse toDto(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .role(member.getRole().name())
                .joinedAt(DatetimeUtil.formatDate(member.getCreatedAt()))
                .updatedAt(DatetimeUtil.formatDate(member.getUpdatedAt()))
                .isAvailable(member.getIsAvailable())
                .emailFlag(member.getEmailFlag())
                .fcmToken(member.getFcmToken())
                .fcmFlag(member.getFcmFlag())
                .build();
    }
}
