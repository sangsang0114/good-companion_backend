package org.sku.zero.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sku.zero.domain.Shop;


@Getter
@NoArgsConstructor
public class ListPriceStoreApiResponseDto {
    @JsonProperty("ListPriceModelStoreService")
    private ListPriceModelStoreService listPriceModelStoreService;

    public ListPriceModelStoreService getListPriceModelStoreService() {
        return listPriceModelStoreService;
    }


    public static class ListPriceModelStoreService {
        private ListPriceStoreApiInfo[] row;

        public ListPriceStoreApiInfo[] getRow() {
            return row;
        }

        public void setRow(ListPriceStoreApiInfo[] row) {
            this.row = row;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListPriceStoreApiInfo {
        @JsonProperty("SH_ID")
        private String id;

        @JsonProperty("SH_NAME")
        private String name;

        @JsonProperty("INDUTY_CODE_SE")
        private String sectorId;

        @JsonProperty("SH_ADDR")
        private String address;

        @JsonProperty("SH_PHONE")
        private String phone;

        @JsonProperty("SH_INFO")
        private String info;

        @JsonProperty("SH_PRIDE")
        private String boast;

        @JsonProperty("SH_PHOTO")
        private String imgUrl;

        @JsonProperty("SH_RCMN")
        private Long recommend;

        public Shop toShopEntity(String regionId, String refinedAddress, String businessHours,
                                  String zipcode, Integer isFranchise) {
            return Shop.builder()
                    .id(id)
                    .name(name)
                    .shopSector(sectorId)
                    .address(refinedAddress)
                    .phone(phone)
                    .info(info)
                    .boast(boast)
                    .shopRegion(regionId)
                    .recommend(recommend)
                    .businessHours(businessHours)
                    .zipcode(zipcode)
                    .isLocalFranchise(isFranchise)
                    .imgUrlPublic(imgUrl)
                    .build();
        }
    }
}