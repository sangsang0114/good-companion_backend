package org.sku.zero.dto.response;

import lombok.*;
import org.sku.zero.domain.ShopLocation;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShopLocationResponse {
    private String latitude;
    private String longitude;

    public static ShopLocationResponse toDto(ShopLocation shopLocation) {
        return ShopLocationResponse.builder()
                .latitude(shopLocation.getLatitude().toString())
                .longitude(shopLocation.getLongitude().toString())
                .build();
    }
}
