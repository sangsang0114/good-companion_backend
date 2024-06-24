package org.sku.zero.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShopCountsByRegionResponse {
    private String regionId;
    private Long count;
}
