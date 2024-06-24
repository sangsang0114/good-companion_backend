package org.sku.zero.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShopCountsBySectorResponse {
    private String sectorId;
    private Long count;
}
