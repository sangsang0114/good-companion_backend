package org.example.dto.external;

import lombok.Builder;
import org.example.domain.ShopLocation;

import java.math.BigDecimal;

@Builder
public record GeoCoderResultDto(String regionId, String latitude, String longitude, String refinedAddress) {
    public static GeoCoderResultDto toDto(GeoCoderApiResponseDto dto) {
        return GeoCoderResultDto.builder()
                .regionId(dto.getResponse().getRefined().getStructure().getLevel4AC())
                .latitude(dto.getResponse().getResult().getPoint().getY())
                .longitude(dto.getResponse().getResult().getPoint().getX())
                .refinedAddress(dto.getResponse().getRefined().getText())
                .build();
    }

    public ShopLocation toEntity(String shopId) {
        return ShopLocation.builder()
                .longitude(new BigDecimal(longitude))
                .latitude(new BigDecimal(latitude))
                .shopId(shopId)
                .build();
    }
}
