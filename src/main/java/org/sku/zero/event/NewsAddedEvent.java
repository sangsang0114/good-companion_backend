package org.sku.zero.event;

import lombok.Getter;
import org.sku.zero.domain.Shop;
import org.sku.zero.domain.ShopNews;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewsAddedEvent extends ApplicationEvent {
    private final ShopNews shopNews;
    private final Shop shop;

    public NewsAddedEvent(Object source, ShopNews shopNews, Shop shop) {
        super(source);
        this.shopNews = shopNews;
        this.shop = shop;
    }
}
