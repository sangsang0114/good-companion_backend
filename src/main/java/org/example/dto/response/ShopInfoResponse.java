package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.Shop;

@Getter
@Setter
@AllArgsConstructor
public class ShopInfoResponse {
    private String id;
    private String name;
    private String address;
    private String businessHours;

    public ShopInfoResponse(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
        this.address = shop.getAddress();
        this.businessHours = shop.getBusinessHours();
    }
}
