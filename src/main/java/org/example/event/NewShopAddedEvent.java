package org.example.event;

import lombok.Getter;
import org.example.dto.request.AddShopRequest;
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
