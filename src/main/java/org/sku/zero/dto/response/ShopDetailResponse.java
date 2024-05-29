package org.sku.zero.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.sku.zero.domain.Shop;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ShopDetailResponse {
    private String shopId;
    private String shopName;
    private String address;
    private String boast;
    private String info;
    private String phone;
    private String sector;
    private String businessHours;
    private Long recommend;
    private Double rate;
    private Integer isAvailable;
    private List<String> shopImgUrls;
    private Integer isLocalFranchise;

    public static ShopDetailResponse toDto(Shop shop, String sector) {
        return ShopDetailResponse.builder()
                .shopId(shop.getId())
                .shopName(shop.getName())
                .address(shop.getAddress())
                .boast(shop.getBoast())
                .info(shop.getInfo())
                .phone(shop.getPhone())
                .sector(sector)
                .businessHours(shop.getBusinessHours())
                .recommend(shop.getRecommend())
                .rate(shop.getRate())
                .isAvailable(shop.getIsAvailable())
                .isLocalFranchise(shop.getIsLocalFranchise())
                .build();
    }

    public static ShopDetailResponse toDto2(Shop shop, String sector, List<String> shopImgUrls) {
        return ShopDetailResponse.builder()
                .shopId(shop.getId())
                .shopName(shop.getName())
                .address(shop.getAddress())
                .boast(shop.getBoast())
                .info(shop.getInfo())
                .phone(shop.getPhone())
                .sector(sector)
                .shopImgUrls(shopImgUrls)
                .businessHours(shop.getBusinessHours())
                .recommend(shop.getRecommend())
                .rate(shop.getRate())
                .isAvailable(shop.getIsAvailable())
                .isLocalFranchise(shop.getIsLocalFranchise())
                .businessHours(shop.getBusinessHours())
                .build();
    }
}
