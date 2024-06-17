package org.sku.zero.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.sku.zero.domain.ShopPending;
import org.sku.zero.util.DatetimeUtil;

@Getter
@Builder
public class ShopPendingResponse {
    private String id;
    private String name;
    private String address;
    private String sectorId;
    private String boast;
    private String info;
    private String imgUrlPublic;
    private String errorInfo;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String memo;

    public static ShopPendingResponse toDto(ShopPending shopPending) {
        return ShopPendingResponse.builder()
                .id(shopPending.getId())
                .name(shopPending.getName())
                .address(shopPending.getAddress())
                .sectorId(shopPending.getShopSector())
                .boast(shopPending.getBoast())
                .info(shopPending.getInfo())
                .imgUrlPublic(shopPending.getImgUrlPublic())
                .status(shopPending.getStatus())
                .createdAt(DatetimeUtil.formatDate(shopPending.getCreatedAt()))
                .updatedAt(DatetimeUtil.formatDate(shopPending.getUpdatedAt()))
                .errorInfo(shopPending.getErrorInfo())
                .memo(shopPending.getMemo())
                .build();
    }
}
