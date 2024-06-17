package org.sku.zero.dto.request;

import org.sku.zero.domain.Shop;
import org.sku.zero.domain.ShopLocation;
import org.sku.zero.domain.ShopPending;

import java.math.BigDecimal;

public record AcceptShopPendingRequest(
        String id, String newAddress, String zipcode, String phone,
        String boast, String businessHours, String info, String memo) {
    public Shop toShopEntity(ShopPending shopPending, Integer isLocalFranchise, String shopRegion) {
        return Shop.builder()
                .id(shopPending.getId())
                .name(shopPending.getName())
                .address(newAddress)
                .phone(phone)
                .shopSector(shopPending.getShopSector())
                .boast(boast)
                .businessHours(businessHours)
                .info(info)
                .imgUrlPublic(shopPending.getImgUrlPublic())
                .isLocalFranchise(isLocalFranchise)
                .zipcode(zipcode)
                .shopRegion(shopRegion)
                .build();
    }

    public ShopLocation toShopLocationEntity(ShopPending shopPending, BigDecimal latitude, BigDecimal longitude) {
        return ShopLocation.builder()
                .shopId(shopPending.getId())
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
