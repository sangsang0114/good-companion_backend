package org.sku.zero.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ListShopRecommendResponse {
    private String shopId;
    private String shopName;
    private String shopAddress;
}
