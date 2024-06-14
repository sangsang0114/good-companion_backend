package org.sku.zero.event;

import lombok.Getter;
import org.sku.zero.dto.request.AddShopRequest;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewShopAddedEvent extends ApplicationEvent {
    private final AddShopRequest dto;
    private final String firstImgUrl;
    private final String shopId;

    public NewShopAddedEvent(Object source, AddShopRequest dto, String firstImgUrl, String shopId) {
        super(source);
        this.dto = dto;
        this.firstImgUrl = firstImgUrl;
        this.shopId = shopId;
    }
}
