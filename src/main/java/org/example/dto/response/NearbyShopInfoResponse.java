package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class NearbyShopInfoResponse {
    private String id;
    private String name;
    private String address;
    private String phone;
    private Double rate;
    private Long recommend;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String sectorId;
    private String imgUrl;
}
