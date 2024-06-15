package org.sku.zero.event;

import lombok.Getter;
import org.sku.zero.domain.Shop;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewShopAddedEvent extends ApplicationEvent {
    private final Shop shop;
    private final String firstImageUrl;

    public NewShopAddedEvent(Object source, Shop shop, String firstImageUrl) {
        super(source);
        this.shop = shop;
        this.firstImageUrl = firstImageUrl;
    }
}
