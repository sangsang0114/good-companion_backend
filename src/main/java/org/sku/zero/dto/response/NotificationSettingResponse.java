package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.Member;

@Builder
@Getter
public class NotificationSettingResponse {
    private Boolean fcmFlag;
    private Boolean emailFlag;

    public static NotificationSettingResponse toDto(Member member) {
        return NotificationSettingResponse.builder()
                .emailFlag(member.getEmailFlag() == 1)
                .fcmFlag(member.getFcmFlag() == 1)
                .build();
    }
}
