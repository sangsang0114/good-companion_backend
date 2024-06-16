package org.sku.zero.dto.external;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FcmSendRequest {
    private String token;
    private String title;
    private String body;
    private String imageUrl;
    private String url;
}
