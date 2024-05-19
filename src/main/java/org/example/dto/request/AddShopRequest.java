package org.example.dto.request;

import org.example.domain.Shop;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record AddShopRequest(String id, String name, String sectorId, String address,
                             String regionId, String phone, String boast, String info,
                             List<MultipartFile> files, String zipcode, Integer isLocalFranchise) {
    public Shop toEntity(String refinedAddress) {
        return Shop.builder()
                .id(id)
                .name(name)
                .shopSector(sectorId)
                .address(refinedAddress)
                .shopRegion(regionId)
                .phone(phone)
                .boast(boast)
                .info(info)
                .zipcode(zipcode)
                .isLocalFranchise(isLocalFranchise)
                .build();
    }
}
