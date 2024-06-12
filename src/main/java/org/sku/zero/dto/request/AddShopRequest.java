package org.sku.zero.dto.request;

import org.sku.zero.domain.Shop;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record AddShopRequest(String id, String name, String sectorId, String address,
                             String regionId, String phone, String boast, String info, String businessHours,
                             List<MultipartFile> files, String zipcode, Integer isLocalFranchise) {
    public Shop toEntity(String refinedAddress, String shopId) {
        return Shop.builder()
                .id(shopId)
                .name(name)
                .shopSector(sectorId)
                .address(refinedAddress)
                .shopRegion(regionId)
                .phone(phone)
                .boast(boast)
                .info(info)
                .zipcode(zipcode)
                .isLocalFranchise(isLocalFranchise)
                .businessHours(businessHours)
                .build();
    }
}
