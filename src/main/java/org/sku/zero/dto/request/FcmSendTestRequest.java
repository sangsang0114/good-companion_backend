package org.sku.zero.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmSendTestRequest {
    private String token;
    private String title;
    private String body;

    @Builder(toBuilder = true)
    public FcmSendTestRequest(String token, String title, String body) {
        this.token = token;
        this.title = title;
        this.body = body;
    }
}
