package org.example.event;

import lombok.Getter;
import org.example.dto.request.AddShopRequest;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewShopAddedEvent extends ApplicationEvent {
    private final AddShopRequest dto;

    public NewShopAddedEvent(Object source, AddShopRequest dto) {
        super(source);
        this.dto = dto;
    }
}
