package org.sku.zero.event;

import lombok.Getter;
import org.sku.zero.dto.request.AddShopRequest;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewShopAddedEvent extends ApplicationEvent {
    private final AddShopRequest dto;
    private final String firstImgUrl;

    public NewShopAddedEvent(Object source, AddShopRequest dto, String firstImgUrl) {
        super(source);
        this.dto = dto;
        this.firstImgUrl = firstImgUrl;
    }
}
