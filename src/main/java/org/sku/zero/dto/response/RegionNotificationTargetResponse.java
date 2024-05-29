package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RegionNotificationTargetResponse {
    List<String> fcmTokens;
    List<String> emails;
}
