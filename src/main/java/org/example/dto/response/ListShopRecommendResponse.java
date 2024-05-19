package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.domain.Shop;
import org.example.domain.ShopRecommend;

@Getter
@Builder
@AllArgsConstructor
public class ListShopRecommendResponse {
    private String shopId;
    private String shopName;
    private String shopAddress;
}
